package com.joon.sunguard_api.publicapi.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AccessLevel;

import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublicApiUtils {

    public static <T> String fetchXmlDataAsString(String baseUrl, String apikey, T requsetDto){

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONNECTION, "keep-alive")
                .build();

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Map<String, String> paramMap = xmlMapper.convertValue(requsetDto, Map.class);
        paramMap.put("serviceKey", apikey);

        try{
            String xmlResponse = webClient.get()
                    .uri(uriBuilder -> {
                        paramMap.forEach(uriBuilder::queryParam);
                        return uriBuilder.build();
                    })
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JSONObject jsonObject = XML.toJSONObject(xmlResponse);

            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            Object itemData = items.get("item");

            return itemData.toString();

        } catch (Exception e) {
            throw new RuntimeException("API 호출 및 데이터 추출 실패 : " + e.getMessage());
        }
    }
}
