package com.joon.sunguard_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF(Cross-Site Request Forgery) 보호 기능을 비활성화합니다.
                // REST API를 사용하거나, 별도의 토큰 기반 인증을 사용할 때 주로 비활성화합니다.
                .csrf(csrf -> csrf.disable())

                // HTTP 요청에 대한 접근 권한을 설정합니다.
                .authorizeHttpRequests(authorize -> authorize
                        // 아래에 명시된 특정 경로들은 인증 없이 누구나 접근할 수 있도록 허용합니다.
                        .requestMatchers(
                                "/",             // 루트 경로
                                "/find-route",   // 경로 탐색 URL
                                "/css/**",       // CSS 파일 경로
                                "/js/**",        // JavaScript 파일 경로
                                "/images/**",    // 이미지 파일 경로
                                "/busstops/**",  // BusstopController의 모든 API 경로
                                "/api/**"        // BusStopViewController의 모든 API 경로
                        ).permitAll()
                        // 위에서 허용한 경로를 제외한 나머지 모든 요청은 인증을 요구합니다.
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
