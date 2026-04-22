package com.project.analyticsservice.service;

import com.project.analyticsservice.dto.AnalyticsEventRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {

    private final ClickRecordsService clickRecordsService;

    @KafkaListener(topics = "url-analytics", groupId = "analytics-group")
    public void consume(AnalyticsEventRequestDto event, Acknowledgment ack) {

        clickRecordsService.saveEvent(event);

        ack.acknowledge();
    }
}
