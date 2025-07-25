package com.joon.sunguard_api.domain.security.repository;

import com.joon.sunguard_api.domain.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Modifying
    @Transactional
    void deleteByRefreshToken(String token);

    RefreshToken findByRefreshToken(String token);
}
