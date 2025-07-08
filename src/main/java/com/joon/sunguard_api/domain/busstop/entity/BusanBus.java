package com.joon.sunguard_api.domain.busstop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "busan_bus")
@Getter
@Setter
public class BusanBus {

    @Id
    @Column(name = "line_id", nullable = false)
    private String lineId;

    @Column(name = "line_no", nullable = false)
    private String lineNo;

    @Column(name = "bus_type")
    private String bus_type;

    @Column(name = "start_point")
    private String startPoint;

    @Column(name = "end_point")
    private String endPoint;

    @Column(name = "company")
    private String company;

    @Column(name = "headway")
    private String headway;

    @Column(name = "headway_norm")
    private String headwayNorm;

    @Column(name = "headway_peak")
    private String headwayPeak;

    @Column(name = "headway_holi")
    private String headwayHoli;

    @Column(name = "first_time")
    private String firstTime;

    @Column(name = "last_time")
    private String lastTime;

}
