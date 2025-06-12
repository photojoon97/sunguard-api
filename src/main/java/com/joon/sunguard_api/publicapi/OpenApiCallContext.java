package com.joon.sunguard_api.publicapi;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OpenApiCallContext {

    private final Map<String, OpenApiCallStrategy> strategies;

    public OpenApiCallContext(Map<String, OpenApiCallStrategy> strategies){
        //map.put("listDtoStrategy", responseListStrategy_객체);
        //스프링은 컴포넌트 스캔과 의존성 주입을 통해 위와 같은 Map 객체를 생성함
        this.strategies = strategies;
    }

    public <R, T> R excute(String strategyName, String key, String url, T requestDto, Class<?> responseDto){
        OpenApiCallStrategy strategy = strategies.get(strategyName);
        if(strategy == null){
            throw  new IllegalArgumentException("Cannot find strategy with name : " +  strategyName);
        }
        return (R) strategy.callApi(key, url, requestDto, responseDto);
    }
}
