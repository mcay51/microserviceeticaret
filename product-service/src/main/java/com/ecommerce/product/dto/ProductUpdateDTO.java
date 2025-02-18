package com.ecommerce.product.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductUpdateDTO {
    private String name;
    
    @Positive
    private Double price;
    
    @Positive
    private Integer stock;
} 