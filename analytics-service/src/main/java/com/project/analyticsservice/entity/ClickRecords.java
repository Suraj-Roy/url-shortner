package com.project.analyticsservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "click_records")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClickRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String shortCode;

    private Instant clickedAt;

    private String ipAddress;

    private String userAgent;

    private String country;

    private String referrer;


}
