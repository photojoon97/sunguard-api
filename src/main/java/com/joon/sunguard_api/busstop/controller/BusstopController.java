package com.joon.sunguard_api.busstop.controller;

import com.joon.sunguard_api.busstop.dto.BusRouteStationInfo;
import com.joon.sunguard_api.busstop.dto.BusArrivalInfoResponse;
import com.joon.sunguard_api.busstop.dto.BusStopSearchRequest;
import com.joon.sunguard_api.busstop.dto.BusStopInfoResponse;
import com.joon.sunguard_api.busstop.service.BusstopService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/busstops")
public class BusstopController {

    private final BusstopService busstopService;

    public BusstopController(BusstopService busstopService){
        this.busstopService = busstopService;
    }


    //정류장 이름으로 버스 정류장 조회
    @GetMapping("/by-name")
    public List<BusStopInfoResponse> searchBusStopsByName(@ModelAttribute BusStopSearchRequest request) {
        return busstopService.searchBusStopsByName(request);
    }

    //현재 좌표를 기준으로 버스 정류장 조회
    @GetMapping("/nearbyBusstops")
    public List<BusStopInfoResponse> searchNearbyBusStops(@ModelAttribute BusStopSearchRequest request){
        return busstopService.searchNearbyBusStops(request);
    }

    //버스 정류장_ID를 기준으로 도착 예정 버스 조회
    //부산버스정보시스템 API 문서 4번 항목 참조
    //요청 데이터 : 정류장 ID
    @GetMapping("/arrivalBusByid")
    public List<BusArrivalInfoResponse> findBusArrivalsByStopId(@RequestParam(value = "stopid") String stopId){
        return busstopService.findBusArrivalsByStopId(stopId);
    }

    //버스 노선_ID를 기준으로 버스의 노선 조회
    //부산버스정보시스템 API 문서 3번 항목 참조
    //요청 데이터 : 노선 ID
    @GetMapping("inqueryOfline")
    public List<BusRouteStationInfo> findBusRouteByLineId(@RequestParam(value = "lineid")String lineid){
        return busstopService.findBusRouteByLineId(lineid);
    }
}
