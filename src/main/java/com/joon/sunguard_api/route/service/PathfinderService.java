package com.joon.sunguard_api.route.service;

import com.joon.sunguard_api.busstop.entity.BusStop;
import com.joon.sunguard_api.route.dto.PathSegment;
import com.joon.sunguard_api.route.dto.RouteNode;
import com.joon.sunguard_api.route.dto.RouteResponse;
import com.joon.sunguard_api.route.dto.RouteStep;
import com.joon.sunguard_api.route.util.AzimuthAngle;
import com.joon.sunguard_api.route.util.CalculateDistance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PathfinderService {

    private final RouteDataService routeDataService;
    private final CalculateDistance calculateDistance;
    private final AzimuthAngle azimuthAngle;
    private static final int MAX_TRANSFERS = 2; // 환승횟수 제한
    private static final double TRANSFER_PENALTY = 1.0; // 환승 페널티 (km)

    /**
     * [수정] 정류장 ID를 기반으로 최적 경로를 탐색하되, 내부적으로는 이름을 사용해
     * 상행/하행 구분 없이 모든 관련 정류장을 후보로 설정합니다.
     */
    public RouteResponse findShortestPath(String startStopId, String endStopId) {
        log.info("findShortestPath 메서드 실행 (ID 기반): {} -> {}", startStopId, endStopId);

        // 1. 전달받은 ID로 정류장 정보 및 이름을 조회합니다.
        BusStop startStop = routeDataService.getStopInfo().get(startStopId);
        BusStop endStop = routeDataService.getStopInfo().get(endStopId);

        if (startStop == null || endStop == null) {
            log.error("출발지 또는 목적지 ID에 해당하는 정류장 정보를 찾을 수 없습니다. startId: {}, endId: {}", startStopId, endStopId);
            throw new IllegalArgumentException("유효하지 않은 출발지 또는 목적지 ID입니다.");
        }

        //출발, 도착 정류장의 이름
        String startStopName = startStop.getStationName();
        String endStopName = endStop.getStationName();

        // 2. 조회한 이름을 사용해 가능한 모든 출발/도착 정류장 ID 목록을 가져옵니다.
        List<String> allStartStopIds = routeDataService.getStopNameToIds().get(startStopName); //츌발역과 이름이 같은 모든 정류장의 ID
        List<String> allEndStopIds = routeDataService.getStopNameToIds().get(endStopName); //도착역과 이름이 같은 모든 정류장의 ID

        if (allStartStopIds == null || allStartStopIds.isEmpty() || allEndStopIds == null || allEndStopIds.isEmpty()) {
            log.error("정류장 이름에 해당하는 ID 목록을 찾을 수 없습니다. startName: {}, endName: {}", startStopName, endStopName);
            // 이 경우는 데이터 로딩에 문제가 있는 경우이므로, 심각한 오류로 처리합니다.
            throw new IllegalStateException("경로 데이터 구성에 문제가 있습니다.");
        }


        PriorityQueue<RouteNode> openSet = new PriorityQueue<>(); //최단거리일 확률이 높은 경로
        Map<PathSegment, PathSegment> cameFrom = new HashMap<>(); //누적된 경로
        Map<PathSegment, Double> gScore = new HashMap<>(); //현재까지 이동한 거리
        Map<String, Double> moveVector = new HashMap<>(); //이동 방향, 거리

        // 휴리스틱(h-score) 계산을 위해 대표 목적지 정류장을 사용합니다.
        BusStop representativeEndStop = endStop;

        // 3. 가능한 모든 출발 정류장에 대해 A* 알고리즘을 초기화합니다.
        for (String currentStartId : allStartStopIds) {
            BusStop currentStartStop = routeDataService.getStopInfo().get(currentStartId);
            for (String lineId : routeDataService.getStopToLines().getOrDefault(currentStartId, Collections.emptyList())) {
                PathSegment startSegment = new PathSegment(currentStartId, lineId, 0);
                gScore.put(startSegment, 0.0);

                double hScore = calculateDistance.getHaversineDistance(
                                                    currentStartStop.getGpsY(), currentStartStop.getGpsX(),
                                                    representativeEndStop.getGpsY(), representativeEndStop.getGpsX()
                                                    );

                openSet.add(new RouteNode(hScore, 0.0, currentStartId, lineId, 0, 0.0, ""));
                cameFrom.put(startSegment, null);
            }
        }

        while (!openSet.isEmpty()) { //openSet이 빌때까지 새로운 경로 탐색
            RouteNode current = openSet.poll();
            String currentStopId = current.getStopId();
            String currentLineId = current.getLineId();
            int currentTransfers = current.getTransfers();
            moveVector.merge(current.getDirection(), current.getDistance(), Double::sum);


            PathSegment currentSegment = new PathSegment(currentStopId, currentLineId, currentTransfers); //현재 정류장

            if (current.getGScore() > gScore.getOrDefault(currentSegment, Double.MAX_VALUE)) {
                continue;
            }

            // 4. [수정] 현재 정류장이 목적지 후보 중 하나인지 확인합니다.
            if (allEndStopIds.contains(currentStopId)) { // 도착 정류장ID들 중 현재 정류장ID와 일치하는 것이 있으면 탐색 종료
                log.info("목적지 도착! 경로를 재구성합니다.");
                //여기서 최장거리 방위각 도출
                //RouteNode : "방위각" : 거리
                Optional<Map.Entry<String, Double>> MaxDirection =
                        moveVector.entrySet().stream().max(Map.Entry.comparingByValue());

                String direction = MaxDirection
                        .map(Map.Entry::getKey)
                        .orElse(" ");
                return reconstructPath(cameFrom, currentSegment, gScore.get(currentSegment), direction);
            }

            BusStop currentStopInfo = routeDataService.getStopInfo().get(currentStopId);

            // 5. 동일 노선으로 다음 정류장 이동
            List<String> routeStops = routeDataService.getLineToStops().get(currentLineId);
            if (routeStops != null) {
                int currentIndex = routeStops.indexOf(currentStopId);
                if (currentIndex != -1 && currentIndex < routeStops.size() - 1) { //현재 위치가 종점이 아닌 경우
                    String nextStopId = routeStops.get(currentIndex + 1); //다음 정류장 가져옴
                    BusStop nextStopInfo = routeDataService.getStopInfo().get(nextStopId); //다음 정류장의 정보

                    //현재 정류장 - 다음 정류장 사이의 거리
                    double distance = calculateDistance.getHaversineDistance(
                                                            currentStopInfo.getGpsY(), currentStopInfo.getGpsX(),
                                                            nextStopInfo.getGpsY(), nextStopInfo.getGpsX()
                                                        );
                    //현재 정류장 - 다음 정류장 사이의 진행 방향
                    String azmithAngle = azimuthAngle.calculateBearing(
                                                            currentStopInfo.getGpsY(), currentStopInfo.getGpsX(),
                                                            nextStopInfo.getGpsY(), nextStopInfo.getGpsX()
                                                        );


                    //출발지부터 방금 찾아낸 다음 노드(nextStopInfo)까지의 총 이동 비용(거리)
                    double tentativeGScore = gScore.get(currentSegment) + distance;

                    PathSegment neighborSegment = new PathSegment(nextStopId, currentLineId, currentTransfers); //"다음에 이동할 수 있는 경로 후보
                    if (tentativeGScore < gScore.getOrDefault(neighborSegment, Double.MAX_VALUE)) { //새로 찾은 경로 (tentativeGScore)와 기존에 알고 있던 경로
                        //neighborSegment 상태로 가는 새로운 최단 경로 기록
                        cameFrom.put(neighborSegment, currentSegment);
                        gScore.put(neighborSegment, tentativeGScore);

                        double hScore = calculateDistance.getHaversineDistance(
                                                            nextStopInfo.getGpsY(), nextStopInfo.getGpsX(),
                                                            representativeEndStop.getGpsY(), representativeEndStop.getGpsX()
                                                            );
                        //우선순위 큐에 새로찾은 경로 저장
                        openSet.add(new RouteNode(tentativeGScore + hScore, tentativeGScore, nextStopId, currentLineId, currentTransfers,distance,azmithAngle));
                    }
                }
            }

            // 6. 현재 정류장에서 환승
            if (currentTransfers < MAX_TRANSFERS) { //최대 환승 횟수 확인
                for (String transferLineId : routeDataService.getStopToLines().getOrDefault(currentStopId, Collections.emptyList())) {
                    if (!transferLineId.equals(currentLineId)) { //타고 온 버스로는 환승 X
                        double tentativeGScore = gScore.get(currentSegment) + TRANSFER_PENALTY;
                        PathSegment neighborSegment = new PathSegment(currentStopId, transferLineId, currentTransfers + 1);

                        if (tentativeGScore < gScore.getOrDefault(neighborSegment, Double.MAX_VALUE)) { //환승한 경로와 비교
                            //새로운 상태 기록
                            cameFrom.put(neighborSegment, currentSegment);
                            gScore.put(neighborSegment, tentativeGScore);

                            double hScore = calculateDistance.getHaversineDistance(
                                                                currentStopInfo.getGpsY(), currentStopInfo.getGpsX(),
                                                                representativeEndStop.getGpsY(), representativeEndStop.getGpsX()
                                                                );

                            openSet.add(new RouteNode(tentativeGScore + hScore, tentativeGScore, currentStopId, transferLineId, currentTransfers + 1,0.0,""));
                        }
                    }
                }
            }
        }
        log.warn("경로를 찾을 수 없습니다: {} -> {}", startStopName, endStopName);
        return null; // 경로 없음
    }

    private RouteResponse reconstructPath(Map<PathSegment, PathSegment> cameFrom, PathSegment lastSegment, double totalDistance, String direction) {
        List<PathSegment> path = new ArrayList<>();
        PathSegment current = lastSegment;

        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);

        if (path.isEmpty()) return null;

        List<RouteStep> steps = new ArrayList<>(); //진행 노선의 경로 정보

        int i = 0;
        //같은 버스를 탄 구간끼리 그룹화
        // i: 현재 그룹의 시작점
        // j: i에서 출발해서, 버스 노선이 바뀌기 전까지 최대한 멀리 가보는 포인터
        while (i < path.size()) {
            PathSegment stepStartSegment = path.get(i);
            String currentLineId = stepStartSegment.getLineId();

            int j = i;
            while (j < path.size() && path.get(j).getLineId().equals(currentLineId)) { // 같은 LindId인 동안 실행
                j++;
            }
            PathSegment stepEndSegment = path.get(j - 1); //해당 노선의 마지막 세그먼트

            List<String> stopIdsInStep = new ArrayList<>(); //정류장 ID 목록
            for (int k = i; k < j; k++) { // 처음부터 해당 노선의 마지막 정류장이 나올때까지 반복
                // 환승 시 중복되는 정류장 ID를 피하기 위해 추가
                if (k > i && path.get(k).getStopId().equals(path.get(k-1).getStopId())) {
                    continue;
                }
                stopIdsInStep.add(path.get(k).getStopId());
            }

            List<String> stopNamesInStep = stopIdsInStep.stream()
                    .map(stopId -> routeDataService.getStopInfo().get(stopId).getStationName())
                    .collect(Collectors.toList());

            RouteStep step = RouteStep.builder()
                    .lineId(currentLineId)
                    .lineNum(routeDataService.getLineInfo().get(currentLineId))
                    .startStopId(stepStartSegment.getStopId())
                    .startStopName(routeDataService.getStopInfo().get(stepStartSegment.getStopId()).getStationName())
                    .endStopId(stepEndSegment.getStopId())
                    .endStopName(routeDataService.getStopInfo().get(stepEndSegment.getStopId()).getStationName())
                    .stopCount(stopIdsInStep.size() > 1 ? stopIdsInStep.size() - 1 : 0)
                    .stops(stopNamesInStep)
                    .build();
            steps.add(step);

            i = j;
        }

        return RouteResponse.builder()
                .steps(steps)
                .transferCount(steps.size() - 1)
                .totalDistance(String.format("%.2f", totalDistance))
                .direction(direction)
                .build();
    }
}