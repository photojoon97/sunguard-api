package com.joon.sunguard_api.domain.busstop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "RoutePaths") // DB 테이블 이름은 그대로 유지
@Getter
@Setter
public class RoutePath { // RoutePaths -> RoutePath
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "path_id")
    private Integer pathId;

    @Column(name = "line_id")
    private String lineId;

    @Column(name = "sequence")
    private Integer sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stop_id", referencedColumnName = "bstop_id")
    private BusStop busStop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", referencedColumnName = "line_id", insertable = false, updatable = false)
    private BusanBus busanBus;
}
