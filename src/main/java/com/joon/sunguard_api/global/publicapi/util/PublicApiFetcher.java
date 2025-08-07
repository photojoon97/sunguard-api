package com.joon.sunguard_api.global.publicapi.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.joon.sunguard_api.global.publicapi.WrapperResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@Slf4j
public class PublicApiFetcher {
    private final WebClient webClient;
    private final XmlMapper xmlMapper;

    public PublicApiFetcher(WebClient webClient){
        this.webClient = webClient;
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public <T> WrapperResponse<T> fetchXmlToWrapper(String api_url, String api_key, Object request, TypeReference<WrapperResponse<T>> typeReference){
        Map<String, String>paramMap = xmlMapper.convertValue(request, Map.class);
        paramMap.put("serviceKey", api_key);

        try {
            // API를 호출하여 XML 응답을 문자열로 받습니다.
            String xmlResponse = webClient.get()
                    .uri(api_url, uriBuilder -> {
                        paramMap.forEach(uriBuilder::queryParam);
                        return uriBuilder.build();
                    })
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(response -> log.debug("Raw XML response: {}", response))
                    .block();

            return xmlMapper.readValue(xmlResponse, typeReference);

        } catch (Exception e) {
            log.error("API call and XML parsing failed", e);
            throw new RuntimeException("API 호출 및 XML 파싱 실패: " + e.getMessage(), e);
        }
    }
}