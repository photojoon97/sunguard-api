
// =================================================================
// 설명: 특정 정류장에 도착하는 버스 정보를 담기 위한 DTO
// =================================================================
package com.joon.sunguard_api.busstop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArrivingBusDto {
    private String lineNo;
    private String lineId;
}
