package com.ecommerce.product.service;

import com.ecommerce.product.dto.OrderRequest;
import com.ecommerce.product.dto.OrderResponse;
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
    private static final String ORDER_SERVICE_URL = "http://order-service/api/orders";
    
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
        try {
            return webClientBuilder.build()
                .post()
                .uri(ORDER_SERVICE_URL)
                .bodyValue(orderRequest)
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .block();
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage());
            throw new RuntimeException("Failed to create order", e);
        }
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