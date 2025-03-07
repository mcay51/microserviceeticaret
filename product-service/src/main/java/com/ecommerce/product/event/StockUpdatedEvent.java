package com.ecommerce.product.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockUpdatedEvent {
    private Long productId;
    private Integer newStock;
    private String message;
} 