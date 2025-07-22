package com.joon.sunguard_api.domain.busstop.dto;

import com.joon.sunguard_api.domain.busstop.entity.BusStop;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusStopWithDistanceDto {
    private final BusStop busStop;
    private final double distance;
}
