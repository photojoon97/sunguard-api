package com.joon.sunguard_api.busstop.service;


import com.joon.sunguard_api.busstop.dto.BusstopRequestDto;
import com.joon.sunguard_api.busstop.dto.BusstopResponseDto;
import com.joon.sunguard_api.config.BusanBusApi;
import com.joon.sunguard_api.publicapi.dto.PublicApiCaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BusstopService {

    private final BusanBusApi busanBusApi;
    private final PublicApiCaller publicApiCaller;

    @Autowired
    public BusstopService(BusanBusApi busanBusApi, PublicApiCaller publicApiCaller) {
        this.busanBusApi = busanBusApi;
        this.publicApiCaller = publicApiCaller;
    }

    public BusstopResponseDto getBusstopList(BusstopRequestDto request){
        String busanBusUrl = busanBusApi.getUrl();
        String busanBusApiKey = busanBusApi.getKey();

        return publicApiCaller.getRequest(request, BusstopResponseDto.class, busanBusUrl, busanBusApiKey);
    }
}
