package com.project.urlservice.service;

import com.project.urlservice.dto.request.AnalyticsEventRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private KafkaTemplate<String, AnalyticsEventRequestDto> kafkaTemplate;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Captor
    private ArgumentCaptor<AnalyticsEventRequestDto> eventCaptor;

    @Nested
    @DisplayName("trackClick")
    class TrackClick {

        @Test
        @DisplayName("should send analytics event to Kafka with correct topic and key")
        void shouldSendEventToKafka() {
            when(request.getHeader("X-Forwarded-For")).thenReturn(null);
            when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0");
            when(request.getHeader("Referer")).thenReturn("https://example.com");
            when(request.getRemoteAddr()).thenReturn("192.168.1.1");
            when(kafkaTemplate.send(eq("url-analytics"), eq("abc123"), any(AnalyticsEventRequestDto.class)))
                    .thenReturn(new CompletableFuture<>());

            analyticsService.trackClick("abc123", request);

            verify(kafkaTemplate).send(eq("url-analytics"), eq("abc123"), eventCaptor.capture());

            AnalyticsEventRequestDto captured = eventCaptor.getValue();
            assertThat(captured.getShortCode()).isEqualTo("abc123");
            assertThat(captured.getIpAddress()).isEqualTo("192.168.1.1");
            assertThat(captured.getUserAgent()).isEqualTo("Mozilla/5.0");
            assertThat(captured.getReferrer()).isEqualTo("https://example.com");
            assertThat(captured.getClickedAt()).isNotNull();
        }

        @Test
        @DisplayName("should handle null User-Agent and Referer headers")
        void shouldHandleNullHeaders() {
            when(request.getHeader("X-Forwarded-For")).thenReturn(null);
            when(request.getHeader("User-Agent")).thenReturn(null);
            when(request.getHeader("Referer")).thenReturn(null);
            when(request.getRemoteAddr()).thenReturn("10.0.0.1");
            when(kafkaTemplate.send(eq("url-analytics"), eq("xyz789"), any(AnalyticsEventRequestDto.class)))
                    .thenReturn(new CompletableFuture<>());

            analyticsService.trackClick("xyz789", request);

            verify(kafkaTemplate).send(eq("url-analytics"), eq("xyz789"), eventCaptor.capture());

            AnalyticsEventRequestDto captured = eventCaptor.getValue();
            assertThat(captured.getUserAgent()).isNull();
            assertThat(captured.getReferrer()).isNull();
        }
    }

    @Nested
    @DisplayName("getClientIp")
    class GetClientIp {

        @Test
        @DisplayName("should extract first IP from X-Forwarded-For header")
        void shouldUseXForwardedFor() {
            when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.50, 70.41.3.18, 150.172.238.178");
            when(request.getHeader("User-Agent")).thenReturn(null);
            when(request.getHeader("Referer")).thenReturn(null);
            when(kafkaTemplate.send(eq("url-analytics"), eq("abc123"), any(AnalyticsEventRequestDto.class)))
                    .thenReturn(new CompletableFuture<>());

            analyticsService.trackClick("abc123", request);

            verify(kafkaTemplate).send(eq("url-analytics"), eq("abc123"), eventCaptor.capture());
            assertThat(eventCaptor.getValue().getIpAddress()).isEqualTo("203.0.113.50");
        }

        @Test
        @DisplayName("should use single X-Forwarded-For value when no comma present")
        void shouldHandleSingleXForwardedFor() {
            when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.50");
            when(request.getHeader("User-Agent")).thenReturn(null);
            when(request.getHeader("Referer")).thenReturn(null);
            when(kafkaTemplate.send(eq("url-analytics"), eq("abc123"), any(AnalyticsEventRequestDto.class)))
                    .thenReturn(new CompletableFuture<>());

            analyticsService.trackClick("abc123", request);

            verify(kafkaTemplate).send(eq("url-analytics"), eq("abc123"), eventCaptor.capture());
            assertThat(eventCaptor.getValue().getIpAddress()).isEqualTo("203.0.113.50");
        }

        @Test
        @DisplayName("should fall back to remoteAddr when X-Forwarded-For is empty")
        void shouldFallbackWhenXForwardedForIsEmpty() {
            when(request.getHeader("X-Forwarded-For")).thenReturn("");
            when(request.getHeader("User-Agent")).thenReturn(null);
            when(request.getHeader("Referer")).thenReturn(null);
            when(request.getRemoteAddr()).thenReturn("127.0.0.1");
            when(kafkaTemplate.send(eq("url-analytics"), eq("abc123"), any(AnalyticsEventRequestDto.class)))
                    .thenReturn(new CompletableFuture<>());

            analyticsService.trackClick("abc123", request);

            verify(kafkaTemplate).send(eq("url-analytics"), eq("abc123"), eventCaptor.capture());
            assertThat(eventCaptor.getValue().getIpAddress()).isEqualTo("127.0.0.1");
        }

        @Test
        @DisplayName("should fall back to remoteAddr when X-Forwarded-For is null")
        void shouldFallbackWhenXForwardedForIsNull() {
            when(request.getHeader("X-Forwarded-For")).thenReturn(null);
            when(request.getHeader("User-Agent")).thenReturn(null);
            when(request.getHeader("Referer")).thenReturn(null);
            when(request.getRemoteAddr()).thenReturn("192.168.0.1");
            when(kafkaTemplate.send(eq("url-analytics"), eq("abc123"), any(AnalyticsEventRequestDto.class)))
                    .thenReturn(new CompletableFuture<>());

            analyticsService.trackClick("abc123", request);

            verify(kafkaTemplate).send(eq("url-analytics"), eq("abc123"), eventCaptor.capture());
            assertThat(eventCaptor.getValue().getIpAddress()).isEqualTo("192.168.0.1");
        }
    }
}