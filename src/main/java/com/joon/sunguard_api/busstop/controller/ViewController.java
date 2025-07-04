package com.joon.sunguard_api.busstop.controller;

import com.joon.sunguard_api.busstop.dto.request.BusStopSearchRequest;
import com.joon.sunguard_api.busstop.dto.response.BusStopInfoResponse;
import com.joon.sunguard_api.busstop.dto.response.RealtimeArrivingBusDto;
import com.joon.sunguard_api.busstop.service.BusstopService;
import com.joon.sunguard_api.route.dto.RouteResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final BusstopService busstopService;

    @GetMapping("/")
    public String mainPage(
            @ModelAttribute BusStopSearchRequest searchRequest,
            @RequestParam(required = false) String bstopId,
            @RequestParam(required = false) String stationName,
            @RequestParam(required = false) String departureId,
            @RequestParam(required = false) String departureName,
            @RequestParam(required = false) String destinationId,
            @RequestParam(required = false) String destinationName,
            Model model) {

        String currentSearchKeyword = (searchRequest.getBstopnm() != null) ? searchRequest.getBstopnm() : "";
        model.addAttribute("searchKeyword", currentSearchKeyword);

        //이름으로 버스 정류장 검색
        if (!currentSearchKeyword.isEmpty()) {
            List<BusStopInfoResponse> searchResults = busstopService.searchBusStopsByName(searchRequest.getBstopnm());

            // searchResults가 비어있지 않을 때만 searchResultContainer를 모델에 추가합니다.
            if (searchResults != null && !searchResults.isEmpty()) {
                //결과를 Map 타입으로 변환해, Mustache에서 같은 데이터가 반복되지 않도록 수정
                Map<String, Object> searchResultContainer = new HashMap<>();
                searchResultContainer.put("keyword", currentSearchKeyword);
                searchResultContainer.put("results", searchResults);

                model.addAttribute("searchResultContainer", searchResultContainer);
            }
        }
        //StopId로 도착정보 조회
        if (bstopId != null && !bstopId.isEmpty()) {
            List<RealtimeArrivingBusDto> arrivalInfo = busstopService.getRaaltimeArrivingBus(bstopId);
            model.addAttribute("selectedStopId", bstopId);
            model.addAttribute("selectedStopName", stationName);
            model.addAttribute("arrivalInfo", arrivalInfo);
        }

        //출발지 설정
        if (departureId != null && departureName != null) {
            model.addAttribute("departure", new BusStopInfoResponse(departureId, departureName));
        }
        //목적지 설정
        if (destinationId != null && destinationName != null) {
            model.addAttribute("destination", new BusStopInfoResponse(destinationId, destinationName));
        }

        return "index";
    }

    @GetMapping("/find-route")
    public String findRoute(
            @RequestParam String departureId, @RequestParam String departureName,
            @RequestParam String destinationId, @RequestParam String destinationName,
            // [추가] 경로 탐색 후에도 검색어 상태를 유지하기 위해 추가
            @RequestParam(required = false) String bstopnm,
            Model model, RedirectAttributes redirectAttributes) {

        if (departureId == null || departureId.isEmpty() || destinationId == null || destinationId.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "출발지와 목적지를 모두 설정해주세요.");
            return "redirect:/";
        }

        // [추가] 경로 탐색 후에도 검색어 입력창의 상태를 유지합니다.
        model.addAttribute("searchKeyword", bstopnm != null ? bstopnm : "");

        log.info("경로 탐색 요청: {} ({}) -> {} ({})", departureName, departureId, destinationName, destinationId);

        RouteResponse routeResult = busstopService.findShortestPath(departureId, destinationId);

        if (routeResult == null) {
            model.addAttribute("errorMessage", "경로를 찾을 수 없습니다. 다른 정류장을 선택해주세요.");
        } else {
            model.addAttribute("routeResult", routeResult);
        }

        model.addAttribute("departure", new BusStopInfoResponse(departureId, departureName));
        model.addAttribute("destination", new BusStopInfoResponse(destinationId, destinationName));

        return "index";
    }
}
