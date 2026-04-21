package com.project.urlservice.mapper;

import com.project.urlservice.dto.request.UrlRequestDto;
import com.project.urlservice.dto.response.UrlResponseDto;
import com.project.urlservice.entity.UrlMapping;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Component
public class UrlMapper {

    public UrlResponseDto mapToUrlResponseDto(UrlMapping urlMapping){
        return UrlResponseDto.builder()
                .shortUrl(urlMapping.getShortUrl())
                .url(urlMapping.getUrl())
                .isActive(urlMapping.getIsActive())
                .createdAt(urlMapping.getCreatedAt())
                .expiresAt(urlMapping.getExpiresAt())
                .build();
    }

    public UrlMapping mapToUrlMapping(UrlRequestDto urlRequestDto){
        return UrlMapping.builder()
                .shortUrl(generateShortUrl(urlRequestDto.getUrl()))
                .url(urlRequestDto.getUrl())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now()
                        .plusDays(urlRequestDto.getExpiresInDays().longValue()))
                .build();
    }

    private String generateShortUrl(String url) {
        SecureRandom random = new SecureRandom();
        String fingerprint = url;

        long numericValue = Math.abs((long) fingerprint.hashCode());

        String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder shortUrl = new StringBuilder();
        if (numericValue == 0) return "0";

        while (numericValue > 0) {
            shortUrl.append(BASE62.charAt((int) (numericValue % 62)));
            numericValue /= 62;
        }

        return shortUrl.reverse().toString();
    }
}
