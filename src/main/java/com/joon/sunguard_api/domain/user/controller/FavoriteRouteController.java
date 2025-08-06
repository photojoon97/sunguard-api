package com.joon.sunguard_api.domain.user.controller;

import com.joon.sunguard_api.domain.security.dto.CustomOAuth2User;
import com.joon.sunguard_api.domain.user.dto.FavoriteRouteRequest;
import com.joon.sunguard_api.domain.user.dto.favoriteroute.response.FavoriteRouteResponse;
import com.joon.sunguard_api.domain.user.service.FavoriteRouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/routes")
@RequiredArgsConstructor
@Slf4j
public class FavoriteRouteController {

    private final FavoriteRouteService favoriteRouteService;

    @PostMapping("/favorite")
    public ResponseEntity<FavoriteRouteResponse> registerFavoriteRoute(@AuthenticationPrincipal CustomOAuth2User user, @RequestBody FavoriteRouteRequest request) {

        FavoriteRouteResponse registeredRoute = favoriteRouteService.registerFavoriteRoute(user, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredRoute);
    }

    @GetMapping("/favorite")
    public ResponseEntity<List<FavoriteRouteResponse>> getFavoriteRoutes(@AuthenticationPrincipal CustomOAuth2User user){
        List<FavoriteRouteResponse> favoriteRoutes = favoriteRouteService.getFavoriteRoutes(user);

        return ResponseEntity.status(HttpStatus.OK).body(favoriteRoutes);
    }

    @DeleteMapping("/favorite")
    public ResponseEntity<String> deleteFavoriteRoute(@AuthenticationPrincipal CustomOAuth2User user, @RequestBody FavoriteRouteRequest request){

        favoriteRouteService.deleteFavoriteRoute(user, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
