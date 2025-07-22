package com.joon.sunguard_api.domain.busstop.controller;

import com.joon.sunguard_api.domain.busstop.dto.request.NearbyStopsRequest;
import com.joon.sunguard_api.domain.busstop.dto.response.BusArrivalResponse;
import com.joon.sunguard_api.domain.busstop.dto.response.BusStopResponse;
import com.joon.sunguard_api.domain.busstop.service.BusstopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/busStops")
@RequiredArgsConstructor
public class Controller {
    private final BusstopService busstopService;


    //정류장 ID로 정류장 조회
    @GetMapping("/stops/{stopId}")
    public ResponseEntity<BusStopResponse> findById(@PathVariable String stopId){
        BusStopResponse result = busstopService.findBusStopByStopId(stopId);
        return ResponseEntity.ok(result);
    }

    //정류장 이름으로 버스 정류장 조회
    @GetMapping("/stops")
    public ResponseEntity<List<BusStopResponse>> findByName(@RequestParam("stopName") String stopName){
        List<BusStopResponse> result = busstopService.findBusStopsByName(stopName);
        return ResponseEntity.ok(result);
    }

    //현재 좌표를 기준으로 근처 버스 정류장 조회
    //요청 데이터 : latitude, longitude
    @GetMapping("/stops/nearby")
    public ResponseEntity<List<BusStopResponse>> searchNearbyBusStops(
            @RequestParam("latitude") String latitude,
            @RequestParam("longitude") String longitude,
            @RequestParam(value = "radius", defaultValue = "0.5") Double radius) {
        NearbyStopsRequest request = new NearbyStopsRequest(latitude, longitude, radius);
        List<BusStopResponse> result = busstopService.searchNearbyBusStops(request);
        return ResponseEntity.ok(result);
    }

    //버스 정류장_ID를 기준으로 도착 예정 버스 조회
    @GetMapping("/stops/{stopId}/arrivals")
    public ResponseEntity<List<BusArrivalResponse>> findBusArrivalsByStopId(@PathVariable String stopId){
        List<BusArrivalResponse> result =  busstopService.getRealtimeArrivingBus(stopId);
        return ResponseEntity.ok(result);
    }
}
