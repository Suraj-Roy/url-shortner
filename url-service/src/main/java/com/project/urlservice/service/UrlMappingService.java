package com.project.urlservice.service;

import com.project.urlservice.dto.request.UrlRequestDto;
import com.project.urlservice.dto.response.UrlResponseDto;
import com.project.urlservice.entity.UrlMapping;
import com.project.urlservice.exception.UrlNotFoundException;
import com.project.urlservice.mapper.UrlMapper;
import com.project.urlservice.repository.UrlMappingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlMappingService {

    private final UrlMappingRepository urlMappingRepository;
    private final UrlMapper urlMapper;

    @CachePut(value = "urls_by_short", key = "#result.shortUrl")
    @Transactional
    public UrlResponseDto createUrl(UrlRequestDto urlRequestDto) {
        Optional<UrlMapping> findUrlMapping = urlMappingRepository
                .findByUrl(urlRequestDto.getUrl());
        if(findUrlMapping .isPresent()){
            return urlMapper.mapToUrlResponseDto(findUrlMapping.get());
        }
        UrlMapping urlMapping = urlMapper.mapToUrlMapping(urlRequestDto);
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        log.info("Url created successfully with id: {}", savedUrlMapping.getId());
        return urlMapper.mapToUrlResponseDto(savedUrlMapping);
    }

    @Cacheable(value = "urls_by_short", key = "#shortUrl")
    public UrlResponseDto getUrl(String shortUrl){
        return urlMappingRepository.findByShortUrl(shortUrl)
                .map(urlMapper::mapToUrlResponseDto)
                .orElseThrow(() -> new UrlNotFoundException("url not found with this shortUrl: " + shortUrl));
    }



    @CacheEvict(value = "urls_by_short", key = "#shortUrl")
    @Transactional
    public void deleteUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException(shortUrl));

        urlMappingRepository.delete(urlMapping);

        log.info("Deleted URL with short code: {}", shortUrl);
    }
}
