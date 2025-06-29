package com.joon.sunguard_api.busstop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusStopArrivalRequestDto {
    private String bstopid; // API 명세에 맞는 파라미터 이름
}