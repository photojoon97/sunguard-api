package com.joon.sunguard_api.domain.busstop.repository;

import com.joon.sunguard_api.domain.busstop.dto.ArrivingBusDto;
import com.joon.sunguard_api.domain.busstop.dto.response.RouteResponse;
import com.joon.sunguard_api.domain.busstop.entity.RoutePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutePathRepository extends JpaRepository<RoutePath, Integer> {

    @Query("SELECT new com.joon.sunguard_api.domain.busstop.dto.response.RouteResponse(rp.sequence, bs.stopName, bs.bstopId, bs.bstopNo) " +
            "FROM RoutePath rp JOIN rp.busStop bs " +
            "WHERE rp.lineId = :lineId " +
            "ORDER BY rp.sequence ASC")
    List<RouteResponse> findRouteDetailsByLineId(@Param("lineId") String lineId);

    @Query("SELECT new com.joon.sunguard_api.domain.busstop.dto.response.RouteResponse(rp.sequence, bs.stopName, null, null) " +
            "FROM RoutePath rp JOIN rp.busanBus bb JOIN rp.busStop bs " +
            "WHERE TRIM(bb.lineNo) = :lineNo " +
            "ORDER BY rp.sequence ASC")
    List<RouteResponse> findSimpleRouteByLineNoWithTrim(@Param("lineNo") String lineNo);

    @Query("SELECT new com.joon.sunguard_api.domain.busstop.dto.ArrivingBusDto(bb.lineId, bb.lineNo) " +
            "FROM RoutePath rp " +
            "JOIN rp.busanBus bb " +
            "WHERE rp.busStop.bstopId = :bstopId")
    List<ArrivingBusDto> findArrivingBusesByStopId(@Param("bstopId") String bstopId);
}
