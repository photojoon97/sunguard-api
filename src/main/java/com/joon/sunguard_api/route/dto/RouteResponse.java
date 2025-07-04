package com.joon.sunguard_api.route.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class RouteResponse {
    // 경로를 구성하는 단계별 정보
    private List<RouteStep> steps;

    // 총 환승 횟수
    private int transferCount;

    // 총 예상 거리 (km)
    private String totalDistance;
}