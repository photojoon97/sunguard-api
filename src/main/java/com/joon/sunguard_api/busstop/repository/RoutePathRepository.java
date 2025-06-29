package com.joon.sunguard_api.busstop.repository;

import com.joon.sunguard_api.busstop.entity.RoutePath;
import com.joon.sunguard_api.busstop.dto.ArrivingBusDto;
import com.joon.sunguard_api.busstop.dto.RouteDetailDto;
import com.joon.sunguard_api.busstop.dto.SimpleRouteDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutePathRepository extends JpaRepository<RoutePath, Integer> { // RoutePaths -> RoutePath

    @Query("SELECT new com.joon.sunguard_api.busstop.dto.RouteDetailDto(rp.sequence, bs.stationName, bs.bstopNo, bs.bstopId) " +
            "FROM RoutePath rp JOIN rp.busStop bs " +
            "WHERE rp.lineId = :lineId " +
            "ORDER BY rp.sequence ASC")
    List<RouteDetailDto> findRouteDetailsByLineId(@Param("lineId") String lineId);

    @Query("SELECT new com.joon.sunguard_api.busstop.dto.SimpleRouteDto(rp.sequence, bs.stationName) " +
            "FROM RoutePath rp JOIN rp.busanBus bb JOIN rp.busStop bs " +
            "WHERE TRIM(bb.lineNo) = :lineNo " + // bb.busNum -> bb.lineNo
            "ORDER BY rp.sequence ASC")
    List<SimpleRouteDto> findSimpleRouteByLineNoWithTrim(@Param("lineNo") String lineNo);

    @Query("SELECT new com.joon.sunguard_api.busstop.dto.ArrivingBusDto(bb.lineNo, bb.lineId) " +
            "FROM RoutePath rp " +
            "JOIN rp.busanBus bb " +
            "WHERE rp.busStop.bstopId = :bstopId")
    List<ArrivingBusDto> findArrivingBusesByStopId(@Param("bstopId") String bstopId);
}

