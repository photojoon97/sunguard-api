package com.joon.sunguard_api.domain.busstop.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // API 호출로 받아오는 불필요한 필드 무시
public class BusArrivalInfoResponse {

    private String bustype; // 버스 타입 (ex 급행)

    @JsonProperty("lineno")
    private String busNo; // 버스 번호

    @JsonProperty("lineid")
    private String lineId; // 노선 ID

    @JsonProperty("min1")
    private int arrivalMinutes; // 도착까지 남은 시(분)

    @JsonProperty("station1")
    private int remainingStation; // 남은 정류소 수

    @JsonProperty("lowplate1")
    private  int lowPlate; // 일반/저상

}
