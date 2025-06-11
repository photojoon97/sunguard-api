package com.joon.sunguard_api.busstop.controller;

import com.joon.sunguard_api.busstop.dto.BusstopRequest;
import com.joon.sunguard_api.busstop.dto.BusstopRequestDto;
import com.joon.sunguard_api.busstop.dto.BusstopResponseDto;
import com.joon.sunguard_api.busstop.service.BusstopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/busstops")
public class BusstopController {

    @Autowired
    public BusstopService busstopService;

    /*
    @GetMapping("/getStopList")
    public BusstopResponseDto getBusstopList(@ModelAttribute BusstopRequestDto requset){
        return busstopService.getBusstopList(requset);
    }
    */

    @GetMapping("/by-name")
    public List<BusstopResponseDto> findBusStopsByName(@ModelAttribute BusstopRequest request) {
        return busstopService.findBusStopsByName(request);
    }
}
