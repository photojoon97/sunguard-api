package com.joon.sunguard_api.domain.user.repository;

import com.joon.sunguard_api.domain.user.entity.FavoriteStops;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteStopsRepository extends JpaRepository<FavoriteStops, Long> {

}
