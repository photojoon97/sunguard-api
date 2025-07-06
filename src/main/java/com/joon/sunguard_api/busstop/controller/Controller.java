package com.joon.sunguard_api.busstop.controller;

import com.joon.sunguard_api.busstop.dto.response.BusStopInfoResponse;
import com.joon.sunguard_api.busstop.service.BusstopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    private final BusstopService busstopService;

    @GetMapping("/stop/{stopId}")
    public ResponseEntity<BusStopInfoResponse> findById(@PathVariable String stopId){
        BusStopInfoResponse searchResult = busstopService.findBusStopByStopId(stopId);
        return ResponseEntity.ok(searchResult);
    }

    @GetMapping("/stops")
    public ResponseEntity<List<BusStopInfoResponse>> findByName(@RequestParam("stopName") String stopName){
        List<BusStopInfoResponse> searchResult = busstopService.findBusStopsByName(stopName);
        return ResponseEntity.ok(searchResult);
    }





}
