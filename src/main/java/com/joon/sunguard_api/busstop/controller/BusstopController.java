package com.joon.sunguard_api.busstop.controller;

import com.joon.sunguard_api.busstop.dto.request.BusStopSearchRequest;
import com.joon.sunguard_api.busstop.dto.response.BusArrivalInfoResponse;
import com.joon.sunguard_api.busstop.dto.response.BusRouteStationInfo;
import com.joon.sunguard_api.busstop.dto.response.BusStopInfoResponse;
import com.joon.sunguard_api.busstop.dto.response.RealtimeArrivingBusDto;
import com.joon.sunguard_api.busstop.service.BusstopService;
import org.springframework.http.ResponseEntity;
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
    //http://localhost:8080/busstops/busStopsByName?bstopnm=안락
    @GetMapping("/busStopsByName")
    public List<BusStopInfoResponse> searchBusStopsByName(@ModelAttribute BusStopSearchRequest request) {
        return busstopService.searchBusStopsByName(request);
    }

    //현재 좌표를 기준으로 근처 버스 정류장 조회
    //http://localhost:8080/busstops/nearbyBusstops?latitude=35.1799&longitude=129.0756
    @GetMapping("/nearbyBusstops")
    public List<BusStopInfoResponse> searchNearbyBusStops(@ModelAttribute BusStopSearchRequest request){
        return busstopService.searchNearbyBusStops(request);
    }

    //버스 정류장_ID를 기준으로 도착 예정 버스 조회
    //부산버스정보시스템 API 문서 4번 항목 참조
    //요청 데이터 : 정류장 ID
    //http://localhost:8080/busstops/arrivalBusByid?stopid=166070203
    @GetMapping("/arrivalBusByid")
    public List<BusArrivalInfoResponse> findBusArrivalsByStopId(@RequestParam(value = "stopid") String stopId){
        return busstopService.findBusArrivalsByStopId(stopId);
    }

    //버스 노선_ID를 기준으로 버스의 노선 조회
    //부산버스정보시스템 API 문서 3번 항목 참조
    //요청 데이터 : 노선 ID
    //http://localhost:8080/busstops/searchLine?lineid=5200155000
    @GetMapping("searchLine")
    public List<BusRouteStationInfo> findBusRouteByLineId(@RequestParam(value = "lineid")String lineid){
        return busstopService.findBusRouteByLineId(lineid);
    }


    //실시간 도착 버스 조회
    // 요청 데이터 : 정거장 ID
    //curl -X GET "http://localhost:8080/busstops/arrival?bstopId=505780000"
    @GetMapping("/arrival")
    public ResponseEntity<List<RealtimeArrivingBusDto>> getRealTimeArrivalInfo(
            @RequestParam String bstopId) {
        List<RealtimeArrivingBusDto> arrivalInfo = busstopService.getRaaltimeArrivingBus(bstopId);
        return ResponseEntity.ok(arrivalInfo);
    }


}
