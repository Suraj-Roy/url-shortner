package com.project.analyticsservice.repository;

import com.project.analyticsservice.entity.ClickRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClickRecordsRepository extends JpaRepository<ClickRecords, String> {
    @Query("SELECT COUNT(c) FROM ClickRecords c WHERE c.shortCode = :shortCode")
    Long countByShortCode(String shortCode);
}