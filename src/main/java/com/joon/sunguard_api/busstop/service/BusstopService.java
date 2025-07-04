package com.joon.sunguard_api.busstop.service;


import com.joon.sunguard_api.busstop.dto.*;
import com.joon.sunguard_api.busstop.dto.request.BusStopArrivalRequestDto;
import com.joon.sunguard_api.busstop.dto.request.BusStopSearchRequest;
import com.joon.sunguard_api.busstop.dto.response.*;
import com.joon.sunguard_api.busstop.entity.BusStop;
import com.joon.sunguard_api.busstop.repository.BusStopRepository;
import com.joon.sunguard_api.busstop.repository.RoutePathRepository;
import com.joon.sunguard_api.config.BusanBusApi;
import com.joon.sunguard_api.publicapi.OpenApiCallContext;
import com.joon.sunguard_api.route.dto.RouteResponse;
import com.joon.sunguard_api.route.service.PathfinderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public List<BusStopInfoResponse> searchBusStopsByName(String satationName){
        /*
        deprecated
        String url = busanBusApi.getUrl().getBase_url();
        String key = busanBusApi.getKey();
        return openApiCallContext.excute("listDtoStrategy", key, url, request, BusStopInfoResponse.class);
        */
        return busStopRepository.findByStationName(satationName);
    }

    //현재 위치를 기준으로 근처 정류장 검색
    // 현재 위치를 기준으로 근처 정류장 검색 (DB 조회 방식)
    @Transactional(readOnly = true)
    public List<BusStopInfoResponse> searchNearbyBusStops(BusStopSearchRequest request) {

        double latitude = Double.parseDouble(request.getLatitude());
        double longitude = Double.parseDouble(request.getLongitude());
        double radius = 0.5; // 500m 반경

        // 수정된 부분 1: 리포지토리 반환 타입에 맞게 변수 타입을 List<NearbyStopDto>로 변경
        List<NearbyStopDto> nearbyStops = busStopRepository.findNearbyStops(
                latitude,
                longitude,
                radius
        );

        // 수정된 부분 2: NearbyStopDto를 BusStopInfoResponse로 변환
        return nearbyStops.stream()
                .map(BusStopInfoResponse::new) // NearbyStopDto -> new BusStopInfoResponse(dto) 호출
                .collect(Collectors.toList());
    }

    /*Deprecated
    //버스 정류장 정보 조회 메서드
    @Transactional(readOnly = true)
    public BusStopInfoResponse findBusStopByStopId(String busStopId) {
        // 1. 엔티티를 조회합니다.
        BusStop busStop = busStopRepository.findById(busStopId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 버스 정류장을 찾을 수 없습니다. id=" + busStopId));

        // 2. DTO 객체를 직접 생성하고, 엔티티의 데이터를 Setter를 사용해 채워 넣습니다.
        BusStopInfoResponse dto = new BusStopInfoResponse();
        dto.setBstopId(busStop.getBstopId());
        dto.setStationName(busStop.getStationName());
        dto.setBstopNo(busStop.getBstopNo());

        // 3. 완성된 DTO를 반환합니다.
        return dto;
    }
    */

        public List<BusArrivalInfoResponse> findBusArrivalsByStopId(String busStopId){
        String url = busanBusApi.getUrl().getArrival_url();
        String key = busanBusApi.getKey();

        Map<String, String> requestParam = new HashMap<>();
        requestParam.put("bstopid", busStopId);

        return openApiCallContext.excute("listDtoStrategy", key, url, requestParam, BusArrivalInfoResponse.class);
    }

    public List<BusRouteStationInfo> findBusRouteByLineId(String lineId){
        String url = busanBusApi.getUrl().getRoute_url();
        String key = busanBusApi.getKey();

        Map<String, String> requestParam = new HashMap<>();
        requestParam.put("lineid", lineId);

        return openApiCallContext.excute("listDtoStrategy", key, url, requestParam, BusRouteStationInfo.class);
    }

    //실시간 버스 도착 정보 조회 서비스
    public List<RealtimeArrivingBusDto> getRaaltimeArrivingBus(String bstopId){
        String url = busanBusApi.getUrl().getArrival_url();
        String key = busanBusApi.getKey();

        List<ArrivingBusDto> dbBuses = routePathRepository.findArrivingBusesByStopId(bstopId);
        /*
        if(dbBuses.isEmpty()){
            return Collections.emptyList(); //결과 없으면 빈 리스트 반환
        }
        */

        BusStopArrivalRequestDto requestDto = new BusStopArrivalRequestDto(bstopId);

        List<ApiBusArrivalInfoDto> apiBuses = openApiCallContext.excute(
                "listDtoStrategy",
                key,
                url,
                requestDto,
                ApiBusArrivalInfoDto.class //응답 DTO 클래스
        );

        //apiBuses 리스트 전체를 탐색하고 일치하는 lineId를 찾는 것은 비효율
        // -> Map으로 변환, Key를 lineId로 지정(상수 시간에 값 찾을 수 있음)
        Map<String, ApiBusArrivalInfoDto> apiBusMap = apiBuses.stream().
                collect(Collectors.toMap(ApiBusArrivalInfoDto::getLineId, info->info, (info1, info2) -> info1));
        log.info("버스 도착 정보 : " + apiBusMap);

        //정류장에 도착하는 버스(dbBuses)의 lineId로 도착 정보를 담고 있는 apiInfo로 업데이트


        return dbBuses.stream()
                .map(dbBus -> {
                    RealtimeArrivingBusDto resultDto = new RealtimeArrivingBusDto(dbBus);
                    ApiBusArrivalInfoDto apiInfo = apiBusMap.get(dbBus.getLineId());
                    log.info("apiInfo : " + apiInfo.getLineId() + "remaining time(min) : " + apiInfo.getRemainingTime());
                    if(apiInfo != null){
                        resultDto.updateRealtimeInfo(apiInfo);
                    }
                    return resultDto;
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RouteResponse findShortestPath(String startId, String endId) {
        log.info("경로 탐색 서비스 호출: {} -> {}", startId, endId);
        RouteResponse response = pathfinderService.findShortestPath(startId, endId);
        if (response == null) {
            // 경로를 찾지 못한 경우에 대한 예외 처리나 응답을 정의할 수 있습니다.
            // 예를 들어, RouteResponse에 상태 필드를 추가하여 "경로 없음"을 표시할 수 있습니다.
            log.warn("경로를 찾지 못했습니다: {} -> {}", startId, endId);
            // 임시로 null을 반환하지만, 실제로는 적절한 응답 객체를 반환하는 것이 좋습니다.
        }
        return response;
    }
}


