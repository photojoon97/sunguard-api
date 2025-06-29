package com.joon.sunguard_api.busstop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiBusArrivalInfoDto {
    @JsonProperty("lineid")
    private String lineId;

    @JsonProperty("min1")
    private String remainingTime;

    @JsonProperty("station1")
    private String remainingStops;
}
