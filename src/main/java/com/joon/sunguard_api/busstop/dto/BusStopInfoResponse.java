package com.joon.sunguard_api.busstop.dto;

import com.joon.sunguard_api.busstop.entity.BusStop;
import lombok.Getter;

@Getter
public class BusStopInfoResponse {
    private String bstopId;
    private String stationName;
    private String bstopNo;
    private Double distance; // 근처 정류장 검색 시 사용

    // Entity -> DTO 변환을 위한 생성자
    public BusStopInfoResponse(BusStop entity) {
        this.bstopId = entity.getBstopId();
        this.stationName = entity.getStationName();
        this.bstopNo = entity.getBstopNo();
    }

    // NearbyStopDto -> DTO 변환을 위한 생성자
    public BusStopInfoResponse(NearbyStopDto dto) {
        this.bstopId = dto.getBstopId();
        this.stationName = dto.getStationName();
        this.distance = dto.getDistance();
    }
}
