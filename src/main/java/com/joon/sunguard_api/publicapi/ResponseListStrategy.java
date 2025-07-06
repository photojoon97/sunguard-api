package com.joon.sunguard_api.publicapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joon.sunguard_api.publicapi.util.PublicApiFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("listDtoStrategy")
public class ResponseListStrategy implements OpenApiCallStrategy {

    private final PublicApiFetcher publicApiFetcher;
    private final ObjectMapper objectMapper;

    public ResponseListStrategy(PublicApiFetcher publicApiFetcher) {
        this.publicApiFetcher = publicApiFetcher;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public <T, C> T callApi(String key, String url, C requestParam, Class<T> responseDto) {
        String jsonData = publicApiFetcher.fechXmlDatatoString(url, key, requestParam);
        log.info("jsonData : " + jsonData);

        try {
            // [수정] API 응답이 단일 객체일 경우를 대비하여,
            // JSON 문자열이 배열의 시작을 의미하는 '[' 문자로 시작하지 않으면 감싸줍니다.
            if (jsonData != null && !jsonData.trim().isEmpty() && !jsonData.trim().startsWith("[")) {
                jsonData = "[" + jsonData + "]";
            }

            // JSON 문자열을 DTO 리스트로 변환합니다.
            List<?> resultList = objectMapper.readValue(
                    jsonData,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, responseDto)
            );

            // [핵심 수정] 변환된 리스트가 비어있는지 확인합니다.
            if (resultList.isEmpty()) {
                log.info("API 결과가 비어있습니다. 빈 리스트를 반환합니다.");
                // 비어있다면, 안전하게 비어있는 리스트를 그대로 반환합니다.
                return (T) new ArrayList<>();
            }

            // 리스트가 비어있지 않을 때만 로그를 출력하고 결과를 반환합니다.
            log.info("Type of resultList<> : " + resultList.get(0).getClass().getName());
            return (T) resultList;

        } catch (Exception e) {
            log.error("Json to Dto List conversion failed : " + e.getMessage());
            throw new RuntimeException("Json to Dto List conversion failed : " + e.getMessage());
        }
    }
}
