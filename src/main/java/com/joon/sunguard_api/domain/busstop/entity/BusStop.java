package com.joon.sunguard_api.domain.busstop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bus_stops")
@Getter
@Setter
public class BusStop {
    @Id
    @Column(name = "bstop_id")
    private String bstopId;

    @Column(name = "station_name")
    private String stopName;

    @Column(name = "bstop_no")
    private String bstopNo; // busStopNum -> bstopNo

    @Column(name = "gps_x")
    private Double gpsX; // longitude -> gpsX

    @Column(name = "gps_y")
    private Double gpsY; // latitude -> gpsY

    @Column(name = "bstop_type")
    private String bstopType;
}
