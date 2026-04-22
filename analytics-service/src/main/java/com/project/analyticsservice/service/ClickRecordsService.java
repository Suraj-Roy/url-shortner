package com.project.analyticsservice.service;

import com.project.analyticsservice.dto.AnalyticsEventRequestDto;
import com.project.analyticsservice.dto.TotalClicksResponse;
import com.project.analyticsservice.entity.ClickRecords;
import com.project.analyticsservice.repository.ClickRecordsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClickRecordsService {

    private final ClickRecordsRepository repository;

    public void saveEvent(AnalyticsEventRequestDto event) {

        ClickRecords record = ClickRecords.builder()
                .shortCode(event.getShortCode())
                .clickedAt(event.getClickedAt())
                .ipAddress(event.getIpAddress())
                .userAgent(event.getUserAgent())
                .referrer(event.getReferrer())
                .country(getCountryFromIp(event.getIpAddress()))
                .build();

        repository.save(record);
    }

    private String getCountryFromIp(String ip) {
        return "IN";
    }

    public TotalClicksResponse getTotalClicks(String shortCode) {
        Long totalClicks =  repository.countByShortCode(shortCode);
        return new TotalClicksResponse(shortCode,totalClicks);
    }
}
