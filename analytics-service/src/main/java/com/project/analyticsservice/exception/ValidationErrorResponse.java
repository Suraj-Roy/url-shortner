package com.project.analyticsservice.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ValidationErrorResponse {

    private String traceId;
    private int status;
    private String error;
    private String message;
    private String path;
    private Instant timestamp;
    Map<String, List<String>> fieldErrors;
}