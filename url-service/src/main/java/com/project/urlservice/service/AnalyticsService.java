package com.project.urlservice.service;

import com.project.urlservice.dto.request.AnalyticsEventRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    private final KafkaTemplate<String, AnalyticsEventRequestDto> kafkaTemplate;

    public void trackClick(String shortCode, HttpServletRequest request) {

        AnalyticsEventRequestDto event = AnalyticsEventRequestDto.builder()
                .shortCode(shortCode)
                .clickedAt(Instant.now())
                .ipAddress(getClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .referrer(request.getHeader("Referer"))
                .build();

        kafkaTemplate.send("url-analytics",shortCode,event)
                .exceptionally(ex -> {
                    log.error("Failed to send analytics", ex);
                    return null;
                });
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty()) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
