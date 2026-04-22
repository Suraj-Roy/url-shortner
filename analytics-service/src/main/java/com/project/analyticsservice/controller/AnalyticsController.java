package com.project.analyticsservice.controller;

import com.project.analyticsservice.dto.TotalClicksResponse;
import com.project.analyticsservice.service.AnalyticsService;
import com.project.analyticsservice.service.ClickRecordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final ClickRecordsService clickRecordsService;

    @GetMapping("/{shortCode}/total-clicks")
    public ResponseEntity<TotalClicksResponse> getTotalClicks(@PathVariable String shortCode) {

        TotalClicksResponse totalClicks = clickRecordsService.getTotalClicks(shortCode);

        return ResponseEntity.ok(totalClicks);
    }
}
