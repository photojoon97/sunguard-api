package com.joon.sunguard_api.busstop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusstopRequest {
    private String bstopnm;
    //private String arsno;

    public BusstopRequest(String bstopnm/*, String arsno*/){
        this.bstopnm = bstopnm;
        //this.arsno = arsno;
    }
}
