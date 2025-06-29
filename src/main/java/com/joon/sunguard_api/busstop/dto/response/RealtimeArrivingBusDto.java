package com.joon.sunguard_api.busstop.dto.response;

import com.joon.sunguard_api.busstop.dto.ApiBusArrivalInfoDto;
import com.joon.sunguard_api.busstop.dto.ArrivingBusDto;
import lombok.Getter;

@Getter
public class RealtimeArrivingBusDto {
    private String lineId;
    private String lineNo;
    private String remainingTime;
    private String remainingStops;

    public RealtimeArrivingBusDto(ArrivingBusDto arrivingBusDb){
        this.lineId = arrivingBusDb.getLineId();
        this.lineNo = arrivingBusDb.getLineNo(); // getBusNum() -> getLineNo()
        this.remainingTime = "정보 없음";
        this.remainingStops = "정보 없음";
    }

    public void updateRealtimeInfo(ApiBusArrivalInfoDto apiInfo){
        String time = apiInfo.getRemainingTime();
        String stops = apiInfo.getRemainingStops();
        this.remainingTime = (time != null && !time.isEmpty()) ? time + "분 남음" : "정보 없음";
        this.remainingStops = (stops != null && !stops.isEmpty()) ? stops + "정거장 남음" : "정보 없음";
    }
}
