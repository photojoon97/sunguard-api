package com.joon.sunguard_api.domain.route.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.joon.sunguard_api.domain.route.dto.solarRequest.SolarRequestDto;
import com.joon.sunguard_api.domain.route.dto.solarRequest.SolarResponseDTO;
import com.joon.sunguard_api.global.config.SolarApi;
import com.joon.sunguard_api.global.publicapi.OpenApiCallContext;
import com.joon.sunguard_api.global.publicapi.WrapperResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RecommendSeat {

    private final SolarApi solarApi;
    private final OpenApiCallContext openApiCallContext;

    public String getSolarInfo(String location, String date){
        String key = solarApi.getKey();
        String url = solarApi.getUrl();

        SolarRequestDto requestDto = new SolarRequestDto(location, date);

        Object rawResult = openApiCallContext.excute(
                "singleResponseStrategy",
                key,
                url,
                requestDto,
                new TypeReference<WrapperResponse<SolarResponseDTO>>() {}
        );

        SolarResponseDTO responseDTO = (rawResult instanceof SolarResponseDTO) ? (SolarResponseDTO) rawResult : null;

        if (responseDTO == null) {
            log.warn("No solar information received from API for location: {} and date: {}", location, date);
            return null; // 또는 예외 처리
        }

        //TODO : 태양 방향 계산 로직

        return null;
    }
}