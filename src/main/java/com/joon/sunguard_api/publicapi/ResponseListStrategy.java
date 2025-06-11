package com.joon.sunguard_api.publicapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joon.sunguard_api.publicapi.util.PublicApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("listDtoStrategy")
public class ResponseListStrategy implements OpenApiCallStrategy {

    @Override
    public <T,C> T callApi(String key, String url, C requestDto, Class<T> responseDto){
        String jsonData = PublicApiUtils.fetchXmlDataAsString(url, key, requestDto);

        try{
            ObjectMapper objectMapper = new ObjectMapper();

            if(!jsonData.trim().startsWith("[")){
                jsonData = "[" + jsonData + "]";
            }

            List<?> resultList = objectMapper.readValue(
                    jsonData,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, responseDto)
            );

            return (T) resultList;
        }catch (Exception e){
            log.error("Json to Dto List conversion failed : " + e.getMessage());
            throw new RuntimeException("Json to Dto List conversion failed : " + e.getMessage());
        }
    }
}
