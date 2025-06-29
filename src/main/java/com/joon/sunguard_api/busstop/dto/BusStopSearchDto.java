
// =================================================================
// 설명: 정류장 이름 검색 결과를 담기 위한 DTO
// =================================================================
package com.joon.sunguard_api.busstop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusStopSearchDto {
    private String stationName;
    private String bstopId;
    private String bstopNo;
}
