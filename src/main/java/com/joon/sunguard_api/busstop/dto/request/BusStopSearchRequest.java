package com.joon.sunguard_api.busstop.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusStopSearchRequest {
    private String bstopnm; // 정류장 이름으로 검색
    private String latitude;  // 좌표로 검색
    private String longitude; // 좌표로 검색
}
