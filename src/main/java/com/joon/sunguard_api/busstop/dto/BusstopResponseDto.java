package com.joon.sunguard_api.busstop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusstopResponseDto {

    @JsonProperty("arsno")
    private String arsno;

    @JsonProperty("bstopid")
    private String bstopid;

    @JsonProperty("bstopnm")
    private String bstopnm;

    @JsonProperty("gpsx")
    private double gpsx;

    @JsonProperty("gpsy")
    private double gpsy;

    @JsonProperty("stoptype")
    private String stoptype;
}
