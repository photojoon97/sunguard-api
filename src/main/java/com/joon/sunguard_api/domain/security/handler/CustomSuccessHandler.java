package com.joon.sunguard_api.domain.security.handler;

import com.joon.sunguard_api.domain.security.dto.CustomOAuth2User;
import com.joon.sunguard_api.domain.security.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String username = customOAuth2User.getUsername();
        String role = customOAuth2User.getAuthorities().iterator().next().getAuthority();

        String token = jwtUtil.createJwt(username, role, 60 * 60 * 60L);

        // Add token to cookie
        response.addCookie(createCookie("Authorization", token));
        
        // Redirect to root
        getRedirectStrategy().sendRedirect(request, response, "/");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        // cookie.setSecure(true); // For HTTPS
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
