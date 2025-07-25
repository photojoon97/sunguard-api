package com.joon.sunguard_api.domain.security.handler;

import com.joon.sunguard_api.domain.security.repository.RefreshTokenRepository;
import com.joon.sunguard_api.domain.security.util.CookieMangement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final CookieMangement cookieMangement;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String refreshToken = cookieMangement.extractTokenFromCookie(request, "refresh-token");

        if(refreshToken == null){
            return;
        }

        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }
}
