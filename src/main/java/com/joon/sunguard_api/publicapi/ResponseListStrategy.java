package com.joon.sunguard_api.publicapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joon.sunguard_api.publicapi.util.PublicApiFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component("listDtoStrategy")
public class ResponseListStrategy implements OpenApiCallStrategy {

    private final PublicApiFetcher publicApiFetcher;
    private final ObjectMapper objectMapper;

    public ResponseListStrategy(PublicApiFetcher publicApiFetcher){
        this.publicApiFetcher = publicApiFetcher;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public <T,C> T callApi(String key, String url, C requestParam, Class<T> responseDto){
        //String jsonData = PublicApiUtils.fetchXmlDataAsString(url, key, requestDto);
        String jsonData = publicApiFetcher.fechXmlDatatoString(url, key, requestParam);
        log.info("jsonData : " + jsonData);

        try{

            if(!jsonData.trim().startsWith("[")){
                jsonData = "[" + jsonData + "]";
            }

            List<?> resultList = objectMapper.readValue(
                    jsonData,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, responseDto)
            );

            log.info("Type of resultList<> : " + resultList.get(0).getClass().getName());
            return (T) resultList;

        }catch (Exception e){
            log.error("Json to Dto List conversion failed : " + e.getMessage());
            throw new RuntimeException("Json to Dto List conversion failed : " + e.getMessage());
        }
    }
}
