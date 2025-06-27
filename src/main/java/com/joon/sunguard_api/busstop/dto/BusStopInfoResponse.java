package com.joon.sunguard_api.busstop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joon.sunguard_api.busstop.domain.BusStopEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 1. 기본 생성자를 추가해주는 Lombok 어노테이션
public class BusStopInfoResponse {

    private String arsno;

    @JsonProperty("bstopid")
    private String busStopId;

    @JsonProperty("bstopnm")
    private String busStopName;

    @JsonProperty("gpsx")
    private double longitude;

    @JsonProperty("gpsy")
    private double latitude;

    @JsonProperty("stoptype")
    private String stopType;

    public BusStopInfoResponse(BusStopEntity entity) {
        this.busStopId = String.valueOf(entity.getBstop_id());
        this.busStopName = entity.getStation_name();
        this.longitude = entity.getGpsX();
        this.latitude = entity.getGpsY();
    }

}
