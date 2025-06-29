
// =================================================================
// 설명 : 공공데이터 API 호출로 반환받은 데이터를 저장할 DTO
// =================================================================
package com.joon.sunguard_api.busstop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class
ApiBusArrivalInfoDto {
    @JsonProperty("lineid")
    private String lineId;

    @JsonProperty("min1")
    private String remainingTime;

    @JsonProperty("station1")
    private String remainingStops;
}
