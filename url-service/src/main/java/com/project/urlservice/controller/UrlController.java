package com.project.urlservice.controller;

import com.project.urlservice.dto.request.UrlRequestDto;
import com.project.urlservice.dto.response.UrlResponseDto;
import com.project.urlservice.service.UrlMappingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URL;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
@Slf4j
public class UrlController {

    private final UrlMappingService urlMappingService;

    @PostMapping
    public ResponseEntity<UrlResponseDto> createUrl(@Valid @RequestBody UrlRequestDto urlRequestDto) {
        UrlResponseDto urlResponseDto = urlMappingService.createUrl(urlRequestDto);
        URI url = URI.create(urlResponseDto.getShortUrl());
        return ResponseEntity.created(url).body(urlResponseDto);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<UrlResponseDto> getUrl(@PathVariable String shortCode) {
        UrlResponseDto urlResponseDto = urlMappingService.getUrl(shortCode);
        return ResponseEntity.ok(urlResponseDto);
    }


    @DeleteMapping("{shortCode}")
    public ResponseEntity<String> deleteUrl(@PathVariable String shortCode) {
        urlMappingService.deleteUrl(shortCode);
        return ResponseEntity.ok("Url deleted successfully");
    }

}
