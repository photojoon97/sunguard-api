package com.joon.sunguard_api.solar.service;

import com.joon.sunguard_api.config.SolarApi;
import com.joon.sunguard_api.publicapi.dto.PublicApiCaller;
import com.joon.sunguard_api.solar.dto.SolarRequestDto;
import com.joon.sunguard_api.solar.dto.SolarResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SolarService {

    private SolarApi solarApi;
    public PublicApiCaller publicApiCaller;

    @Autowired
    public SolarService(SolarApi solarApi, PublicApiCaller publicApiCaller){
        this.solarApi = solarApi;
        this.publicApiCaller = publicApiCaller;
    }


    public SolarResponseDto getSorlarInfo(SolarRequestDto request){
        String SOLAR_URL = solarApi.getUrl();
        String SOLAR_API_KEY = solarApi.getKey();
        return publicApiCaller.getRequest(request, SolarResponseDto.class, SOLAR_URL, SOLAR_API_KEY);
    }
}