package com.project.urlservice.service;

import com.project.urlservice.dto.request.UrlRequestDto;
import com.project.urlservice.dto.response.UrlResponseDto;
import com.project.urlservice.entity.UrlMapping;
import com.project.urlservice.exception.UrlNotFoundException;
import com.project.urlservice.mapper.UrlMapper;
import com.project.urlservice.repository.UrlMappingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UrlMappingService Tests")
class UrlMappingServiceTest {

    @Mock
    private UrlMappingRepository urlMappingRepository;

    @Mock
    private UrlMapper urlMapper;

    @InjectMocks
    private UrlMappingService urlMappingService;

    @Captor
    private ArgumentCaptor<UrlMapping> urlMappingCaptor;


    @Test
    @DisplayName("Should create new URL with UUID id if not exists")
    void createUrl_shouldCreateNewUrl_whenNotExists() {
        UrlRequestDto requestDto = new UrlRequestDto();
        requestDto.setUrl("https://example.com");
        requestDto.setExpiresInDays(7);

        UUID uuid = UUID.randomUUID();

        UrlMapping mappedEntity = UrlMapping.builder()
                .url(requestDto.getUrl())
                .shortUrl("mockedShortUrl")
                .isActive(true)
                .build();

        UrlMapping savedEntity = UrlMapping.builder()
                .id(uuid)
                .url(requestDto.getUrl())
                .shortUrl("mockedShortUrl")
                .isActive(true)
                .build();

        UrlResponseDto responseDto = UrlResponseDto.builder()
                .url(requestDto.getUrl())
                .shortUrl("mockedShortUrl")
                .isActive(true)
                .build();

        given(urlMappingRepository.findByUrl(requestDto.getUrl())).willReturn(Optional.empty());
        given(urlMapper.mapToUrlMapping(requestDto)).willReturn(mappedEntity);
        given(urlMappingRepository.save(mappedEntity)).willReturn(savedEntity);
        given(urlMapper.mapToUrlResponseDto(savedEntity)).willReturn(responseDto);

        UrlResponseDto result = urlMappingService.createUrl(requestDto);

        assertThat(result.getUrl()).isEqualTo("https://example.com");
        assertThat(result.getShortUrl()).isEqualTo("mockedShortUrl");

        verify(urlMappingRepository).save(urlMappingCaptor.capture());
        assertThat(urlMappingCaptor.getValue().getUrl()).isEqualTo("https://example.com");
    }

    @Test
    @DisplayName("Should return existing URL if already present")
    void createUrl_shouldReturnExistingUrl_whenExists() {
        UrlRequestDto requestDto = new UrlRequestDto();
        requestDto.setUrl("https://example.com");

        UUID uuid = UUID.randomUUID();

        UrlMapping existingEntity = UrlMapping.builder()
                .id(uuid)
                .url(requestDto.getUrl())
                .shortUrl("existingShortUrl")
                .isActive(true)
                .build();

        UrlResponseDto responseDto = UrlResponseDto.builder()
                .url(requestDto.getUrl())
                .shortUrl("existingShortUrl")
                .isActive(true)
                .build();

        given(urlMappingRepository.findByUrl(requestDto.getUrl())).willReturn(Optional.of(existingEntity));
        given(urlMapper.mapToUrlResponseDto(existingEntity)).willReturn(responseDto);

        UrlResponseDto result = urlMappingService.createUrl(requestDto);

        assertThat(result.getShortUrl()).isEqualTo("existingShortUrl");

        verify(urlMappingRepository, never()).save(any());
    }


    @Test
    @DisplayName("Should return UrlResponseDto for valid shortUrl")
    void getUrl_shouldReturnUrl_whenShortUrlExists() {
        UUID uuid = UUID.randomUUID();
        String shortUrl = "abc123";

        UrlMapping entity = UrlMapping.builder()
                .id(uuid)
                .url("https://example.com")
                .shortUrl(shortUrl)
                .isActive(true)
                .build();

        UrlResponseDto responseDto = UrlResponseDto.builder()
                .url("https://example.com")
                .shortUrl(shortUrl)
                .isActive(true)
                .build();

        given(urlMappingRepository.findByShortUrl(shortUrl)).willReturn(Optional.of(entity));
        given(urlMapper.mapToUrlResponseDto(entity)).willReturn(responseDto);

        UrlResponseDto result = urlMappingService.getUrl(shortUrl);

        assertThat(result.getShortUrl()).isEqualTo(shortUrl);
    }

    @Test
    @DisplayName("Should throw UrlNotFoundException if shortUrl does not exist")
    void getUrl_shouldThrowException_whenShortUrlNotFound() {
        String shortUrl = "abc123";
        given(urlMappingRepository.findByShortUrl(shortUrl)).willReturn(Optional.empty());

        assertThatThrownBy(() -> urlMappingService.getUrl(shortUrl))
                .isInstanceOf(UrlNotFoundException.class)
                .hasMessageContaining("url not found with this shortUrl");
    }


    @Test
    @DisplayName("Should delete URL if shortUrl exists")
    void deleteUrl_shouldDelete_whenShortUrlExists() {
        UUID uuid = UUID.randomUUID();
        String shortUrl = "abc123";

        UrlMapping entity = UrlMapping.builder()
                .id(uuid)
                .url("https://example.com")
                .shortUrl(shortUrl)
                .isActive(true)
                .build();

        given(urlMappingRepository.findByShortUrl(shortUrl)).willReturn(Optional.of(entity));

        urlMappingService.deleteUrl(shortUrl);

        verify(urlMappingRepository).delete(entity);
    }

    @Test
    @DisplayName("Should throw UrlNotFoundException when deleting non-existent shortUrl")
    void deleteUrl_shouldThrowException_whenShortUrlNotFound() {
        String shortUrl = "abc123";
        given(urlMappingRepository.findByShortUrl(shortUrl)).willReturn(Optional.empty());

        assertThatThrownBy(() -> urlMappingService.deleteUrl(shortUrl))
                .isInstanceOf(UrlNotFoundException.class)
                .hasMessageContaining(shortUrl);

        verify(urlMappingRepository, never()).delete(any());
    }
}