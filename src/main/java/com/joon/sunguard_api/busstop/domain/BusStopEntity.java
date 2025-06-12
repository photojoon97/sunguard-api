package com.joon.sunguard_api.busstop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bus_stops")
@Getter
@Setter
@NoArgsConstructor
public class BusStopEntity {

    @Id
    private Long bstop_id;

    @Column(name = "station_name")
    private String station_name;

    @Column(name = "gps_x")
    private double gpsX; // 경도

    @Column(name = "gps_y")
    private double gpsY; // 위도
}
