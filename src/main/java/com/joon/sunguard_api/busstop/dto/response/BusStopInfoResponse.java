package com.joon.sunguard_api.busstop.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.joon.sunguard_api.busstop.dto.BusStopWithDistance;
import com.joon.sunguard_api.busstop.entity.BusStop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusStopInfoResponse {
    @JsonProperty("bstopid")
    private String bstopId;
    @JsonProperty("bstopnm")
    private String stationName;
    @JsonProperty("arsno")
    private String bstopNo;
    private Double distance;

    // JPQL의 'SELECT new' 구문을 위한 생성자 추가
    public BusStopInfoResponse(String stationName, String bstopId, String bstopNo) {
        this.stationName = stationName;
        this.bstopId = bstopId;
        this.bstopNo = bstopNo;
    }

    public static BusStopInfoResponse from(BusStop entity){
        return BusStopInfoResponse.builder()
                .bstopId(entity.getBstopId())
                .stationName(entity.getStationName())
                .bstopNo(entity.getBstopNo())
                .build();
    }

    public static BusStopInfoResponse from(BusStopWithDistance projection) {
        return BusStopInfoResponse.builder()
                .bstopId(projection.getBstopId())
                .stationName(projection.getStationName())
                .bstopNo(projection.getBstopNo())
                .distance(projection.getDistance()) // 이제 distance 필드를 채울 수 있습니다!
                .build();
    }
}