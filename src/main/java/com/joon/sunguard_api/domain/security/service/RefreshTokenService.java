package com.joon.sunguard_api.domain.security.service;

import com.joon.sunguard_api.domain.security.dto.CustomOAuth2User;
import com.joon.sunguard_api.domain.security.entity.RefreshToken;
import com.joon.sunguard_api.domain.security.entity.UserEntity;
import com.joon.sunguard_api.domain.security.jwt.JWTUtil;
import com.joon.sunguard_api.domain.security.repository.RefreshTokenRepository;
import com.joon.sunguard_api.domain.security.repository.UserRepository;
import com.joon.sunguard_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;


    public boolean validateRefreshToken(String token) {
        try {

            jwtUtil.isExpired(token);

            if (!"refreshToken".equals(jwtUtil.getCategory(token))) {
                return false;
            }

            RefreshToken dbRefreshToken = refreshTokenRepository.findByRefreshToken(token);

            //TODO: 검증 로직 보완

            return dbRefreshToken != null && dbRefreshToken.getRefreshToken().equals(token);
        } catch (Exception e) {
            return false;
        }
    }

    public void saveToken(String username, String token, long refreshTokenExpiration) {

        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        if (user == null) {
            throw new IllegalArgumentException(("User not found :" + username));
        }

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user) // 이제 user는 UserEntity 타입이므로 에러 없음
                .refreshToken(token)
                .expiredAt(LocalDateTime.now().plusNanos(refreshTokenExpiration * 1_000_000))
                .build();

        refreshTokenRepository.save(refreshToken);

    }

    public void deleteExistRefreshToken(CustomOAuth2User user){
        String userName = user.getUsername();
        UserEntity userEntity = userRepository.findByUsername(userName).orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없음 : " + userName));

        refreshTokenRepository.deleteAllByUserId(userEntity.getId());

    }

}
