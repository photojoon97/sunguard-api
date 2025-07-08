package com.joon.sunguard_api.domain.busstop.service;

import com.joon.sunguard_api.domain.busstop.dto.ApiBusArrivalInfoDto;
import com.joon.sunguard_api.domain.busstop.dto.ArrivingBusDto;
import com.joon.sunguard_api.domain.busstop.dto.BusStopWithDistance;
import com.joon.sunguard_api.domain.busstop.dto.request.BusStopArrivalRequestDto;
import com.joon.sunguard_api.domain.busstop.dto.request.NearbyStopsRequestDto;
import com.joon.sunguard_api.domain.busstop.dto.response.BusArrivalResponse;
import com.joon.sunguard_api.domain.busstop.dto.response.BusStopResponse;
import com.joon.sunguard_api.domain.busstop.entity.BusStop;
import com.joon.sunguard_api.domain.busstop.repository.BusStopRepository;
import com.joon.sunguard_api.domain.busstop.repository.RoutePathRepository;
import com.joon.sunguard_api.global.config.BusanBusApi;
import com.joon.sunguard_api.global.exception.BusStopNotFoundException;
import com.joon.sunguard_api.global.publicapi.OpenApiCallContext;
import com.joon.sunguard_api.domain.route.dto.RouteResponse;
import com.joon.sunguard_api.domain.route.service.PathfinderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BusstopService {

    private final BusanBusApi busanBusApi;
    private final OpenApiCallContext openApiCallContext;
    private final BusStopRepository busStopRepository;
    private final RoutePathRepository routePathRepository;
    private final PathfinderService pathfinderService;

    public BusstopService(
            BusanBusApi busanBusApi,
            OpenApiCallContext openApiCallContext,
            BusStopRepository busStopRepository,
            RoutePathRepository routePathRepository,
            PathfinderService pathfinderService
    ) {
        this.busanBusApi = busanBusApi;
        this.openApiCallContext = openApiCallContext;
        this.busStopRepository = busStopRepository;
        this.routePathRepository = routePathRepository;
        this.pathfinderService = pathfinderService;
    }

    //정류장 이름으로 버스 정류장 검색
    public List<BusStopResponse> findBusStopsByName(String stopName){
        List<BusStopResponse> busStops = busStopRepository.findByStopName(stopName);
        if(busStops.isEmpty()){
            throw new BusStopNotFoundException("해당 이름의 정류장을 찾을 ��� 없습니다 : " + stopName);
        }
        return busStops;
    }

    //현재 위치를 기준으로 근처 정류장 검색
    @Transactional(readOnly = true)
    public List<BusStopResponse> searchNearbyBusStops(NearbyStopsRequestDto request) {
        double latitude = Double.parseDouble(request.getLatitude());
        double longitude = Double.parseDouble(request.getLongitude());
        double radius = request.getRadius();
        log.info("Searching for nearby bus stops with latitude: {}, longitude: {}, radius: {}", latitude, longitude, radius);

        List<BusStopWithDistance> nearbyStops = busStopRepository.findNearbyStops(latitude, longitude, radius);
        log.info("Found {} nearby stops in repository.", nearbyStops.size());

        List<BusStopResponse> response = nearbyStops.stream()
                .map(stop -> BusStopResponse.builder()
                        .bstopId(stop.getBstopId())
                        .stationName(stop.getStationName())
                        .bstopNo(stop.getBstopNo())
                        .distance(stop.getDistance())
                        .build())
                .collect(Collectors.toList());
        log.info("Returning {} bus stops in response.", response.size());
        return response;
    }

    public BusStopResponse findBusStopByStopId(String stopId){
        BusStop busStop = busStopRepository.findById(stopId)
                .orElseThrow(() -> new BusStopNotFoundException("해당 ID의 버스 정류장을 찾을 수 없습니다 : " + stopId));
        return BusStopResponse.builder()
                .bstopId(busStop.getBstopId())
                .stationName(busStop.getStopName())
                .bstopNo(busStop.getBstopNo())
                .build();
    }

    //실시간 버스 도착 정보 조회 서비스
    public List<BusArrivalResponse> getRealtimeArrivingBus(String bstopId){
        String url = busanBusApi.getUrl().getArrival_url();
        String key = busanBusApi.getKey();

        // DB에서 해당 정류장에 도착하는 버스 목록 조회 (lineId, lineNo만 포함)
        List<ArrivingBusDto> dbBuses = routePathRepository.findArrivingBusesByStopId(bstopId);

        // 외부 API 호출을 위한 DTO
        BusStopArrivalRequestDto requestDto = new BusStopArrivalRequestDto(bstopId);

        // 외부 API 호출
        List<ApiBusArrivalInfoDto> apiBuses = openApiCallContext.excute(
                "listDtoStrategy",
                key,
                url,
                requestDto,
                ApiBusArrivalInfoDto.class
        );

        // API 결과를 lineId를 키로 하는 Map으로 변환하여 검색 성능 향상
        Map<String, ApiBusArrivalInfoDto> apiBusMap = apiBuses.stream()
                .collect(Collectors.toMap(ApiBusArrivalInfoDto::getLineId, info -> info, (info1, info2) -> info1));

        // DB 조회 결과와 API 조회 결과를 조합하여 최종 응답 DTO 생성
        return dbBuses.stream()
                .map(dbBus -> {
                    ApiBusArrivalInfoDto apiInfo = apiBusMap.get(dbBus.getLineId());
                    String remainingTime = "정보 없음";
                    String remainingStops = "정보 없음";

                    if (apiInfo != null) {
                        remainingTime = apiInfo.getRemainingTime() != null ? apiInfo.getRemainingTime() + "분" : "정보 없음";
                        remainingStops = apiInfo.getRemainingStops() != null ? apiInfo.getRemainingStops() + "개" : "정보 없음";
                    }

                    return BusArrivalResponse.builder()
                            .lineId(dbBus.getLineId())
                            .lineNo(dbBus.getLineNo())
                            .remainingTime(remainingTime)
                            .remainingStops(remainingStops)
                            .build();
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RouteResponse findShortestPath(String startId, String endId) {
        log.info("경로 탐색 서비스 호출: {} -> {}", startId, endId);
        RouteResponse response = pathfinderService.findShortestPath(startId, endId);
        if (response == null) {
            log.warn("경로를 찾지 못했습니다: {} -> {}", startId, endId);
        }
        return response;
    }
}


