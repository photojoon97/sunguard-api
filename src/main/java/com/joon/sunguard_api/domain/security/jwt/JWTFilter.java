package com.joon.sunguard_api.domain.security.jwt;

import com.joon.sunguard_api.domain.security.dto.CustomOAuth2User;
import com.joon.sunguard_api.domain.security.dto.UserDTO;
import com.joon.sunguard_api.domain.security.util.CookieMangement;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final JWTProvider jwtProvider;
    private final CookieMangement cookieMangement;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String accessToken = cookieMangement.extractTokenFromCookie(request, "access_token");

            if (accessToken == null){
                filterChain.doFilter(request,response);
                return;
            }

            //TODO: refresh-token으로 인증 시도 -> 차단

            jwtUtil.isExpired(accessToken);

            String username = jwtUtil.getUsername(accessToken);
            String role = jwtUtil.getRole(accessToken);
            UserDTO userDto = new UserDTO();
            userDto.setUsername(username);
            userDto.setRole(role);

            CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
            Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //logger.info("Valid access token. Set authentication for user: {}", username);


        }catch (ExpiredJwtException e){

            logger.warn("Access token was expired. Attempting to refresh");

            Authentication authentication = jwtProvider.reissueToken(request, response);

            if(authentication == null){
                logger.error("Failed to reissue token. Refresh token might be expired");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired refresh token");
                return;
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

    }
}
