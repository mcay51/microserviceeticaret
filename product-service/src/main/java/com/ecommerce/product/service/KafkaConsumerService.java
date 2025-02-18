package com.ecommerce.product.service;

import com.ecommerce.product.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {
    
    private final ProductService productService;
    
    @KafkaListener(topics = "order-created", groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderCreated(OrderCreatedEvent event) {
        try {
            log.info("Received order created event: {}", event);
            productService.reserveStock(event.getProductId(), event.getQuantity());
            log.info("Stock reserved successfully for order: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("Error processing order created event: {}", e.getMessage());
        }
    }
} 