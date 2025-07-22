package com.joon.sunguard_api.domain.security.jwt;

import com.joon.sunguard_api.domain.security.dto.CustomOAuth2User;
import com.joon.sunguard_api.domain.security.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                    break;
                }
            }
        }

        if (authorization == null) {
            logger.info("token is null");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization;

        if (jwtUtil.isExpired(token)) {
            logger.info("token is expired");
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        UserDTO userDto = new UserDTO();
        userDto.setUsername(username);
        userDto.setRole(role);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
