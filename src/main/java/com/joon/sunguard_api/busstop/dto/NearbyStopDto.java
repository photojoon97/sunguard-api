
// =================================================================
// 설명 : 근처 정류장을 검색결과를 매핑하기 위한 인터페이스
//       인터페이스 기반 프로젝션(Interface-based Projection)
// =================================================================
package com.joon.sunguard_api.busstop.dto;

public interface NearbyStopDto {
    String getBstopId();
    String getStationName();
    Double getDistance();
}
