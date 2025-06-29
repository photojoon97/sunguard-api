package com.joon.sunguard_api.busstop.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // JSON에 있지만 DTO에 없는 필드는 무시
public class BusRouteStationInfo {

    @JsonProperty("lineno")
    private String busNo;     // 버스 번호

    @JsonProperty("bstopnm")
    private String busStopName;    // 정류소명

    @JsonProperty("bstopidx")
    private int busStopIndex;      // 노선 내 정류소 순번


    private String arsno;      // 정류소 번호

    @JsonProperty("nodeid")
    private String busStopId;     // 정류소 ID

    // API 응답에 carno, lowplate 등이 있지만 모든 정류장에 있지는 않으므로
    // 모든 필드를 다 선언할 필요 없이 필요한 것만 선언
}
