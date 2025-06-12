package com.joon.sunguard_api.solar.controller;

import com.joon.sunguard_api.solar.dto.SolarRequestDto;
import com.joon.sunguard_api.solar.dto.SolarResponseDto;
import com.joon.sunguard_api.solar.service.SolarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solarinfo")
public class SolarController {

    @Autowired
    public SolarService solarService;
/*
    @GetMapping("/getSolarInfo")
    public SolarResponseDto getSolarInfo(@ModelAttribute SolarRequestDto request){
        return solarService.getSorlarInfo(request);
    }

 */
}
