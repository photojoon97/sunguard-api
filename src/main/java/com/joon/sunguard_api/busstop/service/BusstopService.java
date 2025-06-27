package com.joon.sunguard_api.busstop.service;


import com.joon.sunguard_api.busstop.domain.BusStopEntity;
import com.joon.sunguard_api.busstop.dto.BusRouteStationInfo;
import com.joon.sunguard_api.busstop.dto.BusArrivalInfoResponse;
import com.joon.sunguard_api.busstop.dto.BusStopSearchRequest;
import com.joon.sunguard_api.busstop.dto.BusStopInfoResponse;
import com.joon.sunguard_api.busstop.repository.BusStopRepository;
import com.joon.sunguard_api.config.BusanBusApi;
import com.joon.sunguard_api.publicapi.OpenApiCallContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BusstopService {

    private final BusanBusApi busanBusApi;
    private final OpenApiCallContext openApiCallContext;
    private final BusStopRepository busStopRepository;

    public BusstopService(BusanBusApi busanBusApi, OpenApiCallContext openApiCallContext, BusStopRepository busStopRepository) {
        this.busanBusApi = busanBusApi;
        this.openApiCallContext = openApiCallContext;
        this.busStopRepository = busStopRepository;
    }

    //정류장 이름으로 버스 정류장 검색
    public List<BusStopInfoResponse> searchBusStopsByName(BusStopSearchRequest request){
        String url = busanBusApi.getUrl().getBase_url();
        String key = busanBusApi.getKey();

        return openApiCallContext.excute("listDtoStrategy", key, url, request, BusStopInfoResponse.class);
    }

    //현재 위치를 기준으로 근처 정류장 검색
    @Transactional(readOnly = true)
    public List<BusStopInfoResponse> searchNearbyBusStops(BusStopSearchRequest request) {

        double latitude = Double.parseDouble(request.getLatitude());
        double longitude = Double.parseDouble(request.getLongitude());

        List<BusStopEntity> nearbyBusStops = busStopRepository.findNearbyBusStops(
                latitude,
                longitude
        );

        return nearbyBusStops.stream()
                .map(BusStopInfoResponse::new) // busStopEntity -> new BusstopResponseDto(busStopEntity)와 동일
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


}


