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

    public <T> String fechXmlDatatoString(String api_url, String api_key, T request){
        Map<String, String>paramMap = xmlMapper.convertValue(request, Map.class);
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

            JSONObject itemsObject = new JSONObject();

            // items"의 내용을 Object 타입으로 받아 타입을 지정하지 않음
            Object itemsValue = body.opt("items"); //    get() 대신 opt()를 사용하여 "items" 키가 없어도 null을 반환하도록 함.

            if (itemsValue instanceof JSONObject) {
                // 3. 만약 진짜 JSONObject라면, 그대로 사용합니다.
                itemsObject = (JSONObject) itemsValue;

            } else if (itemsValue instanceof String) {
                // 4. 만약 문자열이라면,
                String itemsString = (String) itemsValue;

                // 5. 그 문자열이 비어있지 않고, 진짜 JSON 형태인지 한 번 더 확인합니다.
                if (!itemsString.trim().isEmpty() && itemsString.trim().startsWith("{")) {
                    // 유효한 JSON 문자열일 때만 파싱합니다.
                    itemsObject = new JSONObject(itemsString);
                }
                // (만약 비어있는 문자열이라면 아무것도 하지 않으므로, itemsObject는 그대로 비어있습니다.)
            }

            // 6. [핵심] 이제 "item" 데이터를 꺼내기 전에,
            //    itemsObject에 "item" 키가 존재하는지 최종 확인합니다.
            if (itemsObject.has("item")) {
                // 키가 존재하면, 안전하게 데이터를 추출합니다.
                Object itemData = itemsObject.get("item");
                return itemData.toString(); // 추출한 데이터를 문자열로 반환

            } else {
                // "item" 키가 존재하지 않으면, "결과 없음"으로 간주하고
                // 빈 배열 형태의 문자열 "[]"을 반환하여 후속 처리에서 오류가 나지 않도록 합니다.
                return "[]";
            }
        } catch (Exception e){
            throw new RuntimeException("API 호출 및 데이터 추출 실패 : " + e.getMessage());
        }
    }
}
