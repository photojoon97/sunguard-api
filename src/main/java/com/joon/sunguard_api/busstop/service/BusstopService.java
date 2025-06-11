package com.joon.sunguard_api.busstop.service;


import com.joon.sunguard_api.busstop.dto.BusstopRequest;
import com.joon.sunguard_api.busstop.dto.BusstopRequestDto;
import com.joon.sunguard_api.busstop.dto.BusstopResponseDto;
import com.joon.sunguard_api.config.BusanBusApi;
import com.joon.sunguard_api.publicapi.OpenApiCallStrategy;
import com.joon.sunguard_api.publicapi.OpenApiCaller;
import com.joon.sunguard_api.publicapi.dto.PublicApiCaller;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BusstopService {

    private final BusanBusApi busanBusApi;
    private final OpenApiCaller openApiCaller;

    @Autowired
    public BusstopService(BusanBusApi busanBusApi, OpenApiCaller openApiCaller) {
        this.busanBusApi = busanBusApi;
        this.openApiCaller = openApiCaller;
    }

    public List<BusstopResponseDto> findBusStopsByName(BusstopRequest request){
        String url = busanBusApi.getUrl();
        String key = busanBusApi.getKey();

        return openApiCaller.excute("listDtoStrategy", key, url, request, BusstopResponseDto.class);
    }
}
