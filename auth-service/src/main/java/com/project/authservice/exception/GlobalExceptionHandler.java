package com.project.authservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, List<String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        fe -> fe.getField(),
                        Collectors.mapping(fe -> fe.getDefaultMessage(), Collectors.toList())
                ));

        log.warn("Validation failed: path={} fields={}", request.getRequestURI(), fieldErrors.keySet());

        ValidationErrorResponse body = ValidationErrorResponse.builder()
                .traceId(generateTraceId())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Request validation failed")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HandleUserAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
            HandleUserAlreadyExists ex, HttpServletRequest request) {

        log.warn("User already exists: path={}", request.getRequestURI());
        return buildResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex, HttpServletRequest request) {

        log.warn("Bad request: path={} message={}", request.getRequestURI(), ex.getMessage());
        return buildResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(
            NoResourceFoundException ex, HttpServletRequest request) {

        log.warn("Resource not found: path={}", request.getRequestURI());
        return buildErrorResponse("The requested resource was not found", HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex, HttpServletRequest request) {

        log.error("Unexpected error: path={} type={}", request.getRequestURI(),
                ex.getClass().getName(), ex);

        return buildErrorResponse(
                "An unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }


    private ResponseEntity<ErrorResponse> buildResponse(
            Exception ex, HttpStatus status, HttpServletRequest request) {

        return buildErrorResponse(ex.getMessage(), status, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            String message, HttpStatus status, HttpServletRequest request) {

        ErrorResponse body = ErrorResponse.builder()
                .traceId(generateTraceId())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(status).body(body);
    }


    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}