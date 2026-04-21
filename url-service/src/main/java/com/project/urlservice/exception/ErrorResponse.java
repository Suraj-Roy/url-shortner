package com.project.urlservice.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ErrorResponse {

    private String traceId;
    private int status;
    private String error;
    private String message;
    private String path;
    private Instant timestamp;
}