package com.joon.sunguard_api.domain.user.repository;

import com.joon.sunguard_api.domain.security.entity.UserEntity;
import com.joon.sunguard_api.domain.user.entity.FavoriteRoutes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRouteRepository extends JpaRepository<FavoriteRoutes, Long> {

    boolean existsByStartStopIdAndEndStopId(String startStopId, String endStopId);

    List<FavoriteRoutes> findAllByUser(UserEntity userEntity);
}
