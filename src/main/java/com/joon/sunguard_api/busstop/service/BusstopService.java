package com.joon.sunguard_api.busstop.service;


import com.joon.sunguard_api.busstop.domain.BusStopEntity;
import com.joon.sunguard_api.busstop.dto.BusstopRequest;
import com.joon.sunguard_api.busstop.dto.BusstopResponseDto;
import com.joon.sunguard_api.busstop.repository.BusStopRepository;
import com.joon.sunguard_api.config.BusanBusApi;
import com.joon.sunguard_api.publicapi.OpenApiCallContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public List<BusstopResponseDto> findBusStopsByName(BusstopRequest request){
        String url = busanBusApi.getUrl();
        String key = busanBusApi.getKey();

        return openApiCallContext.excute("listDtoStrategy", key, url, request, BusstopResponseDto.class);
    }

    //현재 위치를 기준으로 근처 정류장 검색
    @Transactional(readOnly = true)
    public List<BusstopResponseDto> findNearbyBusStops(BusstopRequest request) {

        double latitude = Double.parseDouble(request.getGpsY());
        double longitude = Double.parseDouble(request.getGpsX());

        List<BusStopEntity> nearbyBusStops = busStopRepository.findNearbyBusStops(
                latitude,
                longitude
        );

        return nearbyBusStops.stream()
                .map(BusstopResponseDto::new) // busStopEntity -> new BusstopResponseDto(busStopEntity)와 동일
                .collect(Collectors.toList());
    }

}


