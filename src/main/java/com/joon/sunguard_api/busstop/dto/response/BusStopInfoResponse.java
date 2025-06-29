package com.joon.sunguard_api.busstop.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.joon.sunguard_api.busstop.dto.NearbyStopDto;
import com.joon.sunguard_api.busstop.entity.BusStop;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // Jackson이 JSON -> DTO 변환 시 사용할 기본 생성자
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusStopInfoResponse {
    @JsonProperty("bstopid")
    private String bstopId;
    @JsonProperty("bstopnm")
    private String stationName;
    @JsonProperty("arsno")
    private String bstopNo;
    private Double distance;

    // Entity -> DTO 변환을 위한 생성자 (내부 사용)
    public BusStopInfoResponse(BusStop entity) {
        this.bstopId = entity.getBstopId();
        this.stationName = entity.getStationName();
        this.bstopNo = entity.getBstopNo();
    }

    // NearbyStopDto -> DTO 변환을 위한 생성자 (내부 사용)
    public BusStopInfoResponse(NearbyStopDto dto) {
        this.bstopId = dto.getBstopId();
        this.stationName = dto.getStationName();
        this.distance = dto.getDistance();
    }
}
