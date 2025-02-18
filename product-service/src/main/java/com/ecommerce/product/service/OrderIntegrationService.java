package com.ecommerce.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderIntegrationService {
    
    private final WebClient.Builder webClientBuilder;
    
    /**
     * Circuit Breaker ile korunan order service çağrısı
     * - slidingWindowSize: Son 10 çağrıyı değerlendir
     * - minimumNumberOfCalls: En az 5 çağrı yapılmış olmalı
     * - failureRateThreshold: %50 hata oranında devre kesici açılır
     * - waitDurationInOpenState: 10 saniye açık kalır
     * - permittedNumberOfCallsInHalfOpenState: Yarı açık durumda 3 çağrıya izin verir
     */
    @CircuitBreaker(name = "orderService", fallbackMethod = "createOrderFallback")
    public OrderResponse createOrder(OrderRequest orderRequest) {
        return webClientBuilder.build()
            .post()
            .uri("http://order-service/api/orders")
            .bodyValue(orderRequest)
            .retrieve()
            .bodyToMono(OrderResponse.class)
            .block();
    }
    
    /**
     * Circuit Breaker açıldığında çalışacak fallback metodu
     * Order service'e ulaşılamadığında alternatif işlem
     */
    public OrderResponse createOrderFallback(OrderRequest orderRequest, Exception ex) {
        log.error("Order service is down! Falling back", ex);
        // Fallback logic: Örneğin kuyruk sistemine ekle
        return new OrderResponse("PENDING", "Order queued for later processing");
    }
} 