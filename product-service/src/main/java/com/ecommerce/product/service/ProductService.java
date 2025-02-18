package com.ecommerce.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.ecommerce.product.domain.Product;
import com.ecommerce.product.dto.ProductCreateDTO;
import com.ecommerce.product.exception.ProductNotFoundException;
import com.ecommerce.product.exception.InsufficientStockException;
import com.ecommerce.product.repository.ProductRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    
    public Product createProduct(ProductCreateDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        
        return productRepository.save(product);
    }
    
    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }
    
    public Product updateStock(Long id, int quantity) {
        return productRepository.findById(id)
            .map(product -> {
                if (product.getStock() < quantity) {
                    throw new InsufficientStockException("Insufficient stock for product: " + id);
                }
                product.setStock(product.getStock() - quantity);
                return productRepository.save(product);
            })
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }
} 