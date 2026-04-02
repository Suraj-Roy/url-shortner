package com.project.urlservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlAlreadyExistsException.class)
    public ResponseEntity<ExceptionDto> handleUrlAlreadyExistsException(UrlAlreadyExistsException ex, WebRequest request) {
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(exceptionDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleUrlNotFoundException(UrlNotFoundException ex, WebRequest request) {
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }
}
