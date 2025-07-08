package com.joon.sunguard_api.domain.busstop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArrivingBusDto {
    private String lineId;
    private String lineNo;
}
