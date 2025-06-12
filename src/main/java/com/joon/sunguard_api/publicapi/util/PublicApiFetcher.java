package com.joon.sunguard_api.publicapi.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class PublicApiFetcher {
    private final WebClient webClient;
    private final XmlMapper xmlMapper;

    public PublicApiFetcher(WebClient webClient){
        this.webClient = webClient;
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public <T> String fechXmlDatatoString(String api_url, String api_key, T requestDto){
        Map<String, String>paramMap = xmlMapper.convertValue(requestDto, Map.class);
        paramMap.put("serviceKey", api_key);

        try{
            String xmlResponse = webClient.get()
                    .uri(api_url, uriBuilder -> {
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
        } catch (Exception e){
            throw new RuntimeException("API 호출 및 데이터 추출 실패 : " + e.getMessage());
        }
    }
}
