package com.joon.sunguard_api.domain.busstop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.joon.sunguard_api.domain.busstop.dto.ApiBusArrivalInfoDto;
import com.joon.sunguard_api.domain.busstop.dto.ArrivingBusDto;
import com.joon.sunguard_api.domain.busstop.dto.BusStopWithDistanceDto;
import com.joon.sunguard_api.domain.busstop.dto.request.BusStopArrivalRequest;
import com.joon.sunguard_api.domain.busstop.dto.request.NearbyStopsRequest;
import com.joon.sunguard_api.domain.busstop.dto.response.BusArrivalResponse;
import com.joon.sunguard_api.domain.busstop.dto.response.BusStopResponse;
import com.joon.sunguard_api.domain.busstop.entity.BusStop;
import com.joon.sunguard_api.domain.busstop.repository.BusStopRepository;
import com.joon.sunguard_api.domain.busstop.repository.RoutePathRepository;
import com.joon.sunguard_api.domain.busstop.util.LocationUtils;
import com.joon.sunguard_api.global.config.BusanBusApi;
import com.joon.sunguard_api.global.exception.BusStopNotFoundException;
import com.joon.sunguard_api.global.publicapi.OpenApiCallContext;
import com.joon.sunguard_api.global.publicapi.WrapperResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BusstopService {

    private final BusanBusApi busanBusApi;
    private final OpenApiCallContext openApiCallContext;
    private final BusStopRepository busStopRepository;
    private final RoutePathRepository routePathRepository;
    private final LocationUtils locationUtils;

    //정류장 이름으로 버스 정류장 검색
    public List<BusStopResponse> findBusStopsByName(String stopName){
        List<BusStop> busStops = busStopRepository.findByStopNameContaining(stopName);
        //List<BusStopResponse> busStops = busStopRepository.findByStopName(stopName);
        if(busStops.isEmpty()){
            throw new BusStopNotFoundException("해당 이름의 정류장을 찾을 정류장을 없습니다 : " + stopName);
        }

        List<BusStopResponse> result = busStops.stream()
                .map(busStop -> BusStopResponse.builder()
                        .stationName(busStop.getStopName())
                        .bstopId(busStop.getBstopId())
                        .bstopNo(busStop.getBstopNo())
                        .build())
                .toList();

        return result;
    }

    //현재 위치를 기준으로 근처 정류장 검색
    @Transactional(readOnly = true)
    public List<BusStopResponse> searchNearbyBusStops(NearbyStopsRequest request) {
        double latitude = Double.parseDouble(request.getLatitude());
        double longitude = Double.parseDouble(request.getLongitude());
        double radius = request.getRadius();

        double latChange = radius / 111.0; // 위도 1도당 약 111km
        double lonChange = radius / (111.0 * Math.cos(Math.toRadians(latitude)));
        double minLat = latitude - latChange;
        double maxLat = latitude + latChange;
        double minLon = longitude - lonChange;
        double maxLon = longitude + lonChange;

        List<BusStop> candidateStops = busStopRepository.findBusStopsInBoundingBox(minLat, maxLat, minLon, maxLon);

        // 3. 서비스 계층에서 정밀한 거리 계산, 필터링, 정렬, DTO 매핑 수행
        return candidateStops.stream()
                // 3-1. 각 정류장까지의 정확한 거리를 계산 (Haversine 공식 등)
                .map(stop -> {
                    double distance = locationUtils.calculateDistance(latitude, longitude, stop.getGpsY(), stop.getGpsX());
                    // 임시 객체나 Pair를 사용하여 정류장과 계산된 거리를 함께 전달
                    return new BusStopWithDistanceDto(stop, distance);
                })
                // 3-2. 반경(radius) 내에 있는 정류장만 필터링 (2차 필터링)
                .filter(stopWithDistance -> stopWithDistance.getDistance() <= radius)
                // 3-3. 거리가 가까운 순으로 정렬
                .sorted(Comparator.comparing(BusStopWithDistanceDto::getDistance))
                // 3-4. 최종적으로 BusStopResponse DTO로 변환
                .map(stopWithDistance -> BusStopResponse.builder()
                        .bstopId(stopWithDistance.getBusStop().getBstopId())
                        .stationName(stopWithDistance.getBusStop().getStopName())
                        .bstopNo(stopWithDistance.getBusStop().getBstopNo())
                        .distance(stopWithDistance.getDistance())
                        .build())
                .collect(Collectors.toList());
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
        BusStopArrivalRequest requestDto = new BusStopArrivalRequest(bstopId);

        // 외부 API 호출
        Object rawResult = openApiCallContext.excute(
                "listDtoStrategy",
                key,
                url,
                requestDto,
                new TypeReference<WrapperResponse<ApiBusArrivalInfoDto>>() {}
        );

        List<ApiBusArrivalInfoDto> apiBuses = (rawResult instanceof List) ? (List<ApiBusArrivalInfoDto>) rawResult : List.of();


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
}


