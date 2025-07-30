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
import com.joon.sunguard_api.global.exception.DuplicateFavoriteException;
import com.joon.sunguard_api.global.exception.FavoriteStopNotFoundException;
import com.joon.sunguard_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteStopsService {

    private final BusStopRepository busStopRepository;
    private final UserRepository userRepository;
    private final FavoriteStopsRepository favoriteStopsRepository;

    @Transactional
    public FavoriteStopDto registerFavoriteStops(CustomOAuth2User user, String stopId) throws DuplicateFavoriteException {
        BusStop stopEntity = busStopRepository.findById(stopId).orElseThrow(() -> new BusStopNotFoundException("해당 ID의 버스 정류장을 찾을 수 없습니다. : " + stopId));


        String username = user.getUsername();

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + username));

        FavoriteStops favoriteStops = FavoriteStops.create(userEntity, stopEntity);

        Long userId = userEntity.getId();

        if (favoriteStopsRepository.existsByUserIdAndStopId(userId, stopId)) {
            throw new DuplicateFavoriteException("이미 등록된 정류장입니다. 정류장 ID : " + stopId);
        }

        favoriteStopsRepository.save(favoriteStops);

        return FavoriteStopDto.builder()
                .stopName(favoriteStops.getStopName())
                .stopId(favoriteStops.getStopId())
                .stopNo(favoriteStops.getStopNo())
                .userName(favoriteStops.getUser().getName())
                .build();
    }

    public List<FavoriteStopDto> getFavoriteStops(CustomOAuth2User user) {
        String username = user.getUsername();

        if (username == null || username.isBlank()) {
            throw new IllegalStateException("인증된 사용자 정보에 username이 없습니다.");
        }

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + username));

        List<FavoriteStops> favoriteStops = favoriteStopsRepository.findByUserId(userEntity.getId()).orElseThrow(() -> new FavoriteStopNotFoundException("즐겨찾기 된 정류장이 없습니다." + username));
        //TODO: DTO 변환 위임

        List<FavoriteStopDto> result = favoriteStops.stream()
                .map(entity -> FavoriteStopDto.builder()
                        .stopName(entity.getStopName())
                        .stopId(entity.getStopId())
                        .stopNo(entity.getStopNo())
                        .build())
                .collect(Collectors.toList());

        return result;
    }
}
