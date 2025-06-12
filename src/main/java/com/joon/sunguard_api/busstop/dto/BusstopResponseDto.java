package com.joon.sunguard_api.busstop.dto;

import com.joon.sunguard_api.busstop.domain.BusStopEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 1. 기본 생성자를 추가해주는 Lombok 어노테이션
public class BusstopResponseDto {

    private String arsno;

    private String bstopid;

    private String bstopnm;

    private double gpsx;

    private double gpsy;

    private String stoptype;

    public BusstopResponseDto(BusStopEntity entity) {
        this.bstopid = String.valueOf(entity.getBstop_id());
        this.bstopnm = entity.getStation_name();
        this.gpsx = entity.getGpsX();
        this.gpsy = entity.getGpsY();
    }

}
