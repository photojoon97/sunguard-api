package com.joon.sunguard_api.domain.busstop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusStopArrivalRequest {
    private String bstopid; // API 명세에 맞는 파라미터 이름
}