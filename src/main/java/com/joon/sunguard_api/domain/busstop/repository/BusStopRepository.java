
// =================================================================
// 설명 : 'BusStop' 엔티티를 위한 레포지토리, 이름으로 버스 정류장을 조회하거나, 근처 버스 정류장을 조회
// =================================================================

package com.joon.sunguard_api.domain.busstop.repository;

import com.joon.sunguard_api.domain.busstop.dto.BusStopWithDistance;
import com.joon.sunguard_api.domain.busstop.dto.response.BusStopResponse;
import com.joon.sunguard_api.domain.busstop.entity.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusStopRepository extends JpaRepository<BusStop, String> {


    //정류장 이름으로 DB에서 정류장 정보 조회
    @Query("SELECT new com.joon.sunguard_api.domain.busstop.dto.response.BusStopResponse(bs.stopName, bs.bstopId, bs.bstopNo, null) " +
            "FROM BusStop bs " +
            "WHERE bs.stopName LIKE %:stopName%")
    List<BusStopResponse> findByStopName(@Param("stopName") String stopName);


    //현재 위치 근처 정류장 조회
    @Query(value = "SELECT " +
            "b.bstop_id AS bstopId, " +
            "b.station_name AS stationName, " +
            "b.bstop_no AS bstopNo, " +
            "(6371 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(b.gps_y)) * COS(RADIANS(b.gps_x) - RADIANS(:longitude)) + SIN(RADIANS(:latitude)) * SIN(RADIANS(b.gps_y)))) AS distance " +
            "FROM bus_stops b " +
            "HAVING distance <= :radius " +
            "ORDER BY distance",
            nativeQuery = true)
    List<BusStopWithDistance> findNearbyStops(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius
    );
}

