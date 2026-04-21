package com.project.urlservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEventRequestDto {

    private String shortCode;
    private Instant clickedAt;
    private String ipAddress;
    private String userAgent;
    private String referrer;
}
