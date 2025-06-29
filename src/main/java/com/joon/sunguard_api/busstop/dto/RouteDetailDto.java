
// =================================================================
// 설명 : 노선 ID로 경로 조회 시, 결과를 담기 위한 DTO
// =================================================================
package com.joon.sunguard_api.busstop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor //JPQL의 new 연산자를 사용하려면 모든 필드를 포함하는 생성자가 필요
public class RouteDetailDto {
    private Integer sequence;
    private String stationName;
    private String bstopNo;
    private String bstopId;
}
