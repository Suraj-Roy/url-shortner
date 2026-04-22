package com.project.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalClicksResponse {
    private String shortCode;
    private long totalClicks;
}
