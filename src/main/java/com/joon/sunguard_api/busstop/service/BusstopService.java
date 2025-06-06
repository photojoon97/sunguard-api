package com.joon.sunguard_api.busstop.service;


import com.joon.sunguard_api.busstop.dto.BusstopRequestDto;
import com.joon.sunguard_api.busstop.dto.BusstopResponseDto;
import com.joon.sunguard_api.publicapi.dto.PublicApiCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BusstopService {

    @Value("${BUSAN_BUS_API_KEY}")
    private String BUSAN_BUS_API_KEY;

    public static final String BUSAN_BUS_URL = "http://apis.data.go.kr/6260000/BusanBIMS/busStopList";

    @Autowired
    public PublicApiCaller publicApiCaller;

    public BusstopResponseDto getBusstopList(BusstopRequestDto request){

        return publicApiCaller.getRequest(request, BusstopResponseDto.class,BUSAN_BUS_URL, BUSAN_BUS_API_KEY);
    }
}
