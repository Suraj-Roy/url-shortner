package com.project.urlservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "url_mapping")
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "short_url", nullable = false, unique = true)
    private String shortUrl;

    @Column(name = "url", nullable = false)
    private String url;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
