package com.joon.sunguard_api.busstop.repository;

import com.joon.sunguard_api.busstop.domain.BusStopEntity;
import com.joon.sunguard_api.busstop.dto.BusstopRequest;
import com.joon.sunguard_api.busstop.dto.BusstopResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BusStopRepository  extends JpaRepository<BusStopEntity, Long> {
    @Query(value = "SELECT *, " +
            "(6371000 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(stop.gps_y)) * " +
            "COS(RADIANS(stop.gps_x) - RADIANS(:longitude)) + SIN(RADIANS(:latitude))" +
            " * SIN(RADIANS(stop.gps_y)))) AS distance " +
            "FROM bus_stops stop " +
            "HAVING distance <= 250 " +
            "ORDER BY distance ASC;",
            nativeQuery = true)
    List<BusStopEntity> findNearbyBusStops(@Param("latitude") double latitude,
                                           @Param("longitude") double longitude);

}
