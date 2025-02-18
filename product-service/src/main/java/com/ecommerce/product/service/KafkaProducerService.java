package com.ecommerce.product.service;

import com.ecommerce.product.event.StockUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String STOCK_TOPIC = "stock-updates";
    
    /**
     * Stok güncellemelerini Kafka'ya gönder
     */
    public void sendStockUpdate(StockUpdatedEvent event) {
        try {
            kafkaTemplate.send(STOCK_TOPIC, event);
            log.info("Stock update event sent successfully: {}", event);
        } catch (Exception e) {
            log.error("Error sending stock update event: {}", e.getMessage());
            throw new RuntimeException("Failed to send stock update event", e);
        }
    }
} 