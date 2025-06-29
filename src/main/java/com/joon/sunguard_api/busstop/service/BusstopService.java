package com.joon.sunguard_api.busstop.service;


import com.joon.sunguard_api.busstop.dto.*;
import com.joon.sunguard_api.busstop.dto.request.BusStopArrivalRequestDto;
import com.joon.sunguard_api.busstop.dto.request.BusStopSearchRequest;
import com.joon.sunguard_api.busstop.dto.response.BusArrivalInfoResponse;
import com.joon.sunguard_api.busstop.dto.response.BusRouteStationInfo;
import com.joon.sunguard_api.busstop.dto.response.BusStopInfoResponse;
import com.joon.sunguard_api.busstop.dto.response.RealtimeArrivingBusDto;
import com.joon.sunguard_api.busstop.repository.BusStopRepository;
import com.joon.sunguard_api.busstop.repository.RoutePathRepository;
import com.joon.sunguard_api.config.BusanBusApi;
import com.joon.sunguard_api.publicapi.OpenApiCallContext;
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

    public BusstopService(BusanBusApi busanBusApi, OpenApiCallContext openApiCallContext, BusStopRepository busStopRepository, RoutePathRepository routePathRepository) {
        this.busanBusApi = busanBusApi;
        this.openApiCallContext = openApiCallContext;
        this.busStopRepository = busStopRepository;
        this.routePathRepository = routePathRepository;
    }

    //정류장 이름으로 버스 정류장 검색
    public List<BusStopInfoResponse> searchBusStopsByName(BusStopSearchRequest request){
        String url = busanBusApi.getUrl().getBase_url();
        String key = busanBusApi.getKey();

        return openApiCallContext.excute("listDtoStrategy", key, url, request, BusStopInfoResponse.class);
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
        if(dbBuses.isEmpty()){
            return Collections.emptyList(); //결과 없으면 빈 리스트 반환
        }

        BusStopArrivalRequestDto requestDto = new BusStopArrivalRequestDto(bstopId);

        List<ApiBusArrivalInfoDto> apiBuses = openApiCallContext.excute(
                "listDtoStrategy",
                key,
                url,
                requestDto,
                ApiBusArrivalInfoDto.class //응답 DTO 클래스
        );

        Map<String, ApiBusArrivalInfoDto> apiBusMap = apiBuses.stream().
                collect(Collectors.toMap(ApiBusArrivalInfoDto::getLineId, info->info, (info1, info2) -> info1));

        return dbBuses.stream()
                .map(dbBus -> {
                    RealtimeArrivingBusDto resultDto = new RealtimeArrivingBusDto(dbBus);
                    ApiBusArrivalInfoDto apiInfo = apiBusMap.get(dbBus.getLineId());
                    if(apiInfo != null){
                        resultDto.updateRealtimeInfo(apiInfo);
                    }
                    return resultDto;
                }).collect(Collectors.toList());
    }
}


