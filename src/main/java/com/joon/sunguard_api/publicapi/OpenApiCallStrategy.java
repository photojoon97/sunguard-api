package com.joon.sunguard_api.publicapi;

public interface OpenApiCallStrategy {
   <T, C>T callApi(String key, String url, C requestDto, Class<T> responseDto);
}
