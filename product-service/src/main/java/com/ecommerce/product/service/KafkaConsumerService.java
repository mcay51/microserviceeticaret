package com.ecommerce.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {
    
    private final ProductService productService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    /**
     * Order service'den gelen siparişleri dinle
     */
    @KafkaListener(topics = "order-created", groupId = "product-service-group")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received order created event: {}", event);
        try {
            productService.updateStock(event.getProductId(), event.getQuantity());
            log.info("Stock updated successfully for order: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("Error processing order event", e);
            // Hata durumunda order service'e bilgi gönder
            kafkaTemplate.send("order-status", new OrderStatusEvent(
                event.getOrderId(), 
                "FAILED",
                "Stock update failed: " + e.getMessage()
            ));
        }
    }
} 