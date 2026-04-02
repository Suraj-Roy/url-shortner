package com.project.urlservice.repository;

import com.project.urlservice.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, UUID> {
        Optional<UrlMapping> findByUrl(String url);

        Optional<UrlMapping> findByShortUrl(String shortUrl);
}
