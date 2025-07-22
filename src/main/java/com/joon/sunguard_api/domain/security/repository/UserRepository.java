package com.joon.sunguard_api.domain.security.repository;

import com.joon.sunguard_api.domain.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
}
