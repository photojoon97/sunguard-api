package com.joon.sunguard_api.domain.busstop.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class RouteResponse {

    // --- 필수 필드 ---
    private final Integer sequence;     // 노선 순서
    private final String stationName;   // 정류장 이름

    // --- 선택 필드 ---
    private final String bstopId;       // 정류장 ID
    private final String bstopNo;       // 정류장 번호

    @Builder
    public RouteResponse(@NonNull Integer sequence, @NonNull String stationName, String bstopId, String bstopNo) {
        this.sequence = sequence;
        this.stationName = stationName;
        this.bstopId = bstopId;
        this.bstopNo = bstopNo;
    }
}
