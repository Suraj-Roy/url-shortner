package com.project.urlservice.dto.request;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
public class UrlRequestDto {

    @NotBlank(message = "Url is required")
    @URL(message = "Url is invalid")
    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "Url must start with http:// or https://"
    )
    private String url;

    private Boolean isActive;

    @Min(value = 1, message = "Expiry must be at least 1 day")
    @Max(value = 365, message = "Expiry cannot exceed 1 year")
    private Integer expiresInDays;
}
