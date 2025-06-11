package com.joon.sunguard_api.busstop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusstopsResponse {
    private List<BusstopResponseDto> Busstops;

}
