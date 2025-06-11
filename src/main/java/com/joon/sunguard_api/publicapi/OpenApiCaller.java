package com.joon.sunguard_api.publicapi;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OpenApiCaller {

    private final Map<String, OpenApiCallStrategy> strategies;

    public OpenApiCaller(Map<String, OpenApiCallStrategy> strategies){
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
