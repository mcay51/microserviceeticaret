package com.ecommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductCreateDTO {
    @NotBlank
    private String name;
    
    @Positive
    private Double price;
    
    @Positive
    private Integer stock;
} 