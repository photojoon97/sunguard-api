package com.joon.sunguard_api.domain.user.service;

import com.joon.sunguard_api.domain.busstop.entity.BusStop;
import com.joon.sunguard_api.domain.busstop.repository.BusStopRepository;
import com.joon.sunguard_api.domain.security.dto.CustomOAuth2User;
import com.joon.sunguard_api.domain.security.entity.UserEntity;
import com.joon.sunguard_api.domain.security.repository.UserRepository;
import com.joon.sunguard_api.domain.user.dto.FavoriteStopDto;
import com.joon.sunguard_api.domain.user.entity.FavoriteStops;
import com.joon.sunguard_api.domain.user.repository.FavoriteStopsRepository;
import com.joon.sunguard_api.global.exception.BusStopNotFoundException;
import com.joon.sunguard_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteStopsService {

    private final BusStopRepository busStopRepository;
    private final UserRepository userRepository;
    private final FavoriteStopsRepository favoriteStopsRepository;

    @Transactional
    public FavoriteStopDto registerFavoriteStops(CustomOAuth2User user, String stopId) {
        BusStop stopEntity = busStopRepository.findById(stopId).orElseThrow(() -> new BusStopNotFoundException("해당 ID의 버스 정류장을 찾을 수 없습니다. : " + stopId));

//        String userEmail = user.getAttributes().get("email").toString();
//        String userName = user.getAttributes().get("username").toString();

        // 2. JWT에서 사용자 이름(username)을 가져옴
        String username = user.getUsername();

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + username));


        //UserEntity userEntity = userRepository.findByUsernameAndEmail(userEmail, userName);
        // TODO: userEntity가 null일 경우에 대한 예외 처리 추가 필요

        FavoriteStops favoriteStops = FavoriteStops.create(userEntity, stopEntity);

        favoriteStopsRepository.save(favoriteStops);

        return FavoriteStopDto.builder()
                .stopName(favoriteStops.getStopName())
                .stopId(favoriteStops.getStopId())
                .stopNo(favoriteStops.getStopNo())
                .userName(favoriteStops.getUser().getName())
                .build();
    }
}
