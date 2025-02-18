package com.ecommerce.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    /**
     * Stok güncellemelerini Kafka'ya gönder
     */
    public void sendStockUpdateEvent(StockUpdatedEvent event) {
        try {
            kafkaTemplate.send("stock-updates", event)
                .addCallback(
                    result -> log.info("Stock update event sent successfully for product: {}", 
                        event.getProductId()),
                    ex -> log.error("Failed to send stock update event", ex)
                );
        } catch (Exception e) {
            log.error("Error while sending stock update event", e);
            throw new KafkaProducerException("Failed to send stock update event", e);
        }
    }
} 