package com.project.urlservice.controller;

import com.project.urlservice.dto.request.AnalyticsEventRequestDto;
import com.project.urlservice.dto.response.UrlResponseDto;
import com.project.urlservice.service.AnalyticsService;
import com.project.urlservice.service.UrlMappingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/url/public/v1")
public class RedirectController {
    private final UrlMappingService urlMappingService;
    private final AnalyticsService analyticsService;


    @GetMapping("/{shortCode}")
    public void redirectUrl(@PathVariable String shortCode,HttpServletResponse response
            ,HttpServletRequest request) throws IOException {


        UrlResponseDto urlResponseDto = urlMappingService.getUrl(shortCode);
        analyticsService.trackClick(shortCode, request);
        response.sendRedirect(urlResponseDto.getUrl());
    }
}
