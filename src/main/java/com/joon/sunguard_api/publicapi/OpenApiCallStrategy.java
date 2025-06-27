package com.joon.sunguard_api.publicapi;

public interface OpenApiCallStrategy {
   <T, C>T callApi(String key, String url, C requestParam, Class<T> responseDto);
}
