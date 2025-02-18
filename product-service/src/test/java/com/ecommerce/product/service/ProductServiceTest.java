package com.ecommerce.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductService productService;
    
    @Test
    void shouldCreateProductSuccessfully() {
        // given
        ProductCreateDTO dto = new ProductCreateDTO("Test Product", new BigDecimal("100.00"), 10);
        Product product = new Product();
        product.setId(1L);
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        
        when(productRepository.save(any(Product.class))).thenReturn(product);
        
        // when
        Product created = productService.createProduct(dto);
        
        // then
        assertNotNull(created);
        assertEquals(dto.getName(), created.getName());
        assertEquals(dto.getPrice(), created.getPrice());
        assertEquals(dto.getStock(), created.getStock());
        
        verify(productRepository).save(any(Product.class));
    }
} 