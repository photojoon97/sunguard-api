package com.joon.sunguard_api.domain.user.controller;

import com.joon.sunguard_api.domain.security.dto.CustomOAuth2User;
import com.joon.sunguard_api.domain.user.dto.FavoriteStopDto;
import com.joon.sunguard_api.domain.user.service.FavoriteStopsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final FavoriteStopsService favoriteStopsService;

    @GetMapping("/favorite/stops")
    public ResponseEntity<List<FavoriteStopDto>> getFavoriteStops(@AuthenticationPrincipal CustomOAuth2User user){

        if (user == null) {
            throw new IllegalStateException("인증된 사용자 정보가 없습니다.");
        }

        List<FavoriteStopDto> favoriteStopDto = favoriteStopsService.getFavoriteStops(user);
        return ResponseEntity.ok(favoriteStopDto);
    }


    @PostMapping("/favorite/stops/{stopId}")
    public ResponseEntity registerFavoriteStops(@PathVariable("stopId") String stopId, @AuthenticationPrincipal CustomOAuth2User user) {
        FavoriteStopDto favoriteStops = favoriteStopsService.registerFavoriteStops(user, stopId);

        return ResponseEntity.ok(favoriteStops);
    }

    //@DeleteMapping("/favorite/stops/{stopId}")


    //@GetMapping("/favorite/route")

    //@PostMapping("/favorite/route")

}
