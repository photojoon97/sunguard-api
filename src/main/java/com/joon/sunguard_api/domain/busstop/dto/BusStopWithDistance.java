package com.joon.sunguard_api.domain.busstop.dto;

// JPQL/Native Query의 조회 결과를 담을 인터페이스
public interface BusStopWithDistance {

    // SELECT 절의 별칭(alias)과 메소드 이름이 일치해야 합니다.
    String getBstopId();
    String getStationName();
    String getBstopNo();
    Double getDistance();
}
