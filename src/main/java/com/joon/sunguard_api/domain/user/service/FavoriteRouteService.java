package com.joon.sunguard_api.domain.user.service;

import com.joon.sunguard_api.domain.busstop.entity.BusStop;
import com.joon.sunguard_api.domain.busstop.repository.BusStopRepository;
import com.joon.sunguard_api.domain.security.dto.CustomOAuth2User;
import com.joon.sunguard_api.domain.security.entity.UserEntity;
import com.joon.sunguard_api.domain.security.repository.UserRepository;
import com.joon.sunguard_api.domain.user.dto.FavoriteRouteRequest;
import com.joon.sunguard_api.domain.user.dto.favoriteroute.response.FavoriteRouteResponse;
import com.joon.sunguard_api.domain.user.entity.FavoriteRoutes;
import com.joon.sunguard_api.domain.user.repository.FavoriteRouteRepository;
import com.joon.sunguard_api.global.exception.BusStopNotFoundException;
import com.joon.sunguard_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteRouteService {

    private final FavoriteRouteRepository favoriteRouteRepository;
    private final BusStopRepository busStopRepository;
    private final UserRepository userRepository;


    public FavoriteRouteResponse registerFavoriteRoute(CustomOAuth2User user, FavoriteRouteRequest favoriteRoute) {

        String username = user.getUsername();
        String startStopId = favoriteRoute.getStartStopId();
        String endStopId = favoriteRoute.getEndStopId();

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. : " + username));
        BusStop startBusStop = busStopRepository.findByBstopId(startStopId).orElseThrow(() -> new BusStopNotFoundException("정류장을 찾을 수 없습니다 : " + startStopId));
        BusStop endBusStop = busStopRepository.findByBstopId(endStopId).orElseThrow(() -> new BusStopNotFoundException("정류장을 찾을 수 없습니다 : " + endStopId));

        FavoriteRoutes favoriteRoutes = new FavoriteRoutes(userEntity, startBusStop, endBusStop);

        if (favoriteRouteRepository.existsByStartStopIdAndEndStopId(startBusStop.getBstopId(), endBusStop.getBstopId())) {
            //TODO : 이미 존재하는 즐겨찾는 경로 예외처리
            return null;
        } else {
            log.info("favoriteRoutes : " + favoriteRoutes);
            favoriteRouteRepository.save(favoriteRoutes);
        }

        return FavoriteRouteResponse.builder()
                .username(username)
                .startStopId(startBusStop.getBstopId())
                .startStopName(startBusStop.getStopName())
                .endStopId(endBusStop.getBstopId())
                .endStopName(endBusStop.getStopName())
                .build();
    }

    public List<FavoriteRouteResponse> getFavoriteRoutes(CustomOAuth2User user) {
        String username = user.getUsername();
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. : " + username));

        List<FavoriteRoutes> entity = favoriteRouteRepository.findAllByUser(userEntity);

        List<FavoriteRouteResponse> favoriteRouteResponse = entity.stream()
                .map(route -> FavoriteRouteResponse.builder()
                                .username(route.getUser().getUsername())
                                .startStopId(route.getStartStopId())
                                .startStopName(route.getStartStopName())
                                .endStopId(route.getEndStopId())
                                .endStopName(route.getEndStopName())
                                .build()
                        ).collect(Collectors.toList());

        return  favoriteRouteResponse;
    }
}
