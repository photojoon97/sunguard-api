package com.joon.sunguard_api.busstop.dto.response;

import com.joon.sunguard_api.busstop.dto.ArrivingBusDto;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class UserRoute {
    private List<ArrivingBusDto> busList;
    private List<BusRouteStationInfo> nodeList;
    private Map<String, Double> resultVector; //이동방향 벡터(추후, 버스의 어느 좌석에 앉아야 햇빛을 최대한 덜 보는지 판단하기 위해
}
