package com.project.authservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtTokenResponse {
    private String token;
    private String type;
    private String validUntil;
}
