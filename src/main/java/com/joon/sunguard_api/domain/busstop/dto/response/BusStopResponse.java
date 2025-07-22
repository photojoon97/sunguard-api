package com.joon.sunguard_api.domain.busstop.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class BusStopResponse {

    // --- 필수 필드 ---
    private final String stationName; // 정류장 이름
    private final String bstopId;     // 정류장 ID (API 호출용)
    private final String bstopNo;     // 정류장 번호 (사용자 확인용)

    // --- 선택 필드 ---
    private final Double distance;    // 현재 위치로부터의 거리 (단위: km)

    @Builder
    public BusStopResponse(String stationName, String bstopId, String bstopNo, Double distance) {
        this.stationName = stationName;
        this.bstopId = bstopId;
        this.bstopNo = bstopNo;
        this.distance = distance;
    }
}
