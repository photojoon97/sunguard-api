package com.joon.sunguard_api.busstop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusstopRequest {
    private String bstopnm;
    private String arsno;
    private String gpsX;
    private String gpsY;


    public BusstopRequest(String bstopnm, String arsno, String gpsX, String gpsY){
        this.bstopnm = bstopnm;
        this.arsno = arsno;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
    }
}
