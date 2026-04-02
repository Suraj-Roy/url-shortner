package com.project.urlservice.dto.response;

import com.project.urlservice.entity.UrlMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UrlResponseDto {
    private String shortUrl;
    private String url;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;


}
