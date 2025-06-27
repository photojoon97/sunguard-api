package com.joon.sunguard_api.busstop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class BusStopSearchRequest {

    @JsonProperty("bstopnm")
    private String busStopName;

    @JsonProperty("arsno")
    private String arsNo;

    @JsonProperty("gpsX")
    private String longitude;

    @JsonProperty("gpsY")
    private String latitude;


    public BusStopSearchRequest(String busStopName, String arsNo, String longitude, String latitude){
        this.busStopName = busStopName;
        this.arsNo = arsNo;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
