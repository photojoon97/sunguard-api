
// =================================================================
// 설명 : 버스 번호로 경로 조회 시, 결과를 담기 위한 DTO
// =================================================================
package com.joon.sunguard_api.busstop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleRouteDto {
    private Integer sequence;
    private String stationName;
}
