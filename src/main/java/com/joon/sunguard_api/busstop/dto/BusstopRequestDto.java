package com.joon.sunguard_api.busstop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusstopRequestDto {
    private String bstopnm;

    private String arsno;

    private int pageNo;

    private int numOfRows;

    // 전체 생성자
    public BusstopRequestDto(String bstopnm, String arsno, int pageNo, int numOfRows) {
        this.bstopnm = bstopnm;
        this.arsno = arsno;
        this.pageNo = pageNo;
        this.numOfRows = numOfRows;
    }
}
