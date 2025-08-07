package com.joon.sunguard_api.global.publicapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.joon.sunguard_api.global.publicapi.util.PublicApiFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("singleResponseStrategy")
@RequiredArgsConstructor
public class SingleResponseStrategy implements OpenApiCallStrategy {

    private final PublicApiFetcher publicApiFetcher;

    @Override
    public <T, R> Object callApi(String key, String url, R request, TypeReference<WrapperResponse<T>> typeReference) {
        try {
            WrapperResponse<T> result = publicApiFetcher.fetchXmlToWrapper(url, key, request, typeReference);
            List<T> itemList = result.getItemList();

            if (itemList != null && !itemList.isEmpty()) {
                T firstItem = itemList.get(0);
                log.info("API call successful. Fetched single item: {}", firstItem.toString());
                return firstItem;
            } else {
                log.warn("API call returned no items.");
                return null;
            }
        } catch (Exception e) {
            log.error("XML to DTO conversion failed", e);
            throw new RuntimeException("XML to DTO conversion failed: " + e.getMessage());
        }
    }
}