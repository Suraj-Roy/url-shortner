package com.project.urlservice.controller;

import com.project.urlservice.dto.response.UrlResponseDto;
import com.project.urlservice.service.UrlMappingService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RedirectController {
    private final UrlMappingService urlMappingService;

    @GetMapping("/{shortCode}")
    public void redirectUrl(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        UrlResponseDto urlResponseDto = urlMappingService.getUrl(shortCode);
        response.sendRedirect(urlResponseDto.getUrl());
    }
}
