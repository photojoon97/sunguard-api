package com.joon.sunguard_api.busstop.controller;

import com.joon.sunguard_api.busstop.dto.BusstopRequest;
import com.joon.sunguard_api.busstop.dto.BusstopResponseDto;
import com.joon.sunguard_api.busstop.service.BusstopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<BusstopResponseDto> findBusstopsByName(@ModelAttribute BusstopRequest request) {
        return busstopService.findBusStopsByName(request);
    }

    //현재 좌표를 기준으로 버스 정류장 조회
    @GetMapping("/nearbyBusstops")
    public List<BusstopResponseDto> findNearbyBusstops(@ModelAttribute BusstopRequest request){
        return busstopService.findNearbyBusStops(request);
    }

    //버스 정류장_ID를 기준으로 도착 예정 버스 조회

    //버스 노선_ID를 기준으로 버스의 노선 조회


}
