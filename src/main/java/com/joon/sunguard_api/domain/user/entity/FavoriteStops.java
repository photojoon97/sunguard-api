package com.joon.sunguard_api.domain.user.entity;

import com.joon.sunguard_api.domain.busstop.entity.BusStop;
import com.joon.sunguard_api.domain.security.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class FavoriteStops {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String stopId;

    @Column(nullable = false)
    private String stopNo;

    @Column(nullable = false)
    private String stopName;

    public FavoriteStops(UserEntity user, String stopId, String stopNo, String stopName){
        this.user = user;
        this.stopId = stopId;
        this.stopNo = stopNo;
        this.stopName = stopName;
    }

    public static FavoriteStops create(UserEntity userEntity, BusStop stopEntity) {
        return new FavoriteStops(userEntity,stopEntity.getBstopId(), stopEntity.getBstopNo(), stopEntity.getStopName());
    }
}
