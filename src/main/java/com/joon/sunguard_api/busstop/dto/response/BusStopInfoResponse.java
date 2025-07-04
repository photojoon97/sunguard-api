package com.joon.sunguard_api.busstop.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.joon.sunguard_api.busstop.dto.BusStopSearchDto;
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

    /**
     * Controller에서 ID와 이름만으로 간단히 객체를 생성하기 위해 추가된 생성자입니다.
     * @param bstopId 정류장 ID
     * @param stationName 정류장 이름
     */
    public BusStopInfoResponse(String bstopId, String stationName) {
        this.bstopId = bstopId;
        this.stationName = stationName;
    }

    public BusStopInfoResponse(String stationName, String bstopId, String bstopNo) {
        this.bstopId = bstopId;
        this.stationName = stationName;
        this.bstopNo = bstopNo;
    }

    public BusStopInfoResponse(BusStopSearchDto dto) {
        this.bstopId = dto.getBstopId();
        this.stationName = dto.getStationName();
        this.bstopNo = dto.getBstopNo();
    }

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