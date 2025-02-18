package com.ecommerce.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.ecommerce.product.domain.Product;
import com.ecommerce.product.dto.ProductCreateDTO;
import com.ecommerce.product.exception.ProductNotFoundException;
import com.ecommerce.product.exception.InsufficientStockException;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.dto.OrderRequest;
import com.ecommerce.product.dto.OrderResponse;
import com.ecommerce.product.service.OrderIntegrationService;
import lombok.extern.slf4j.Slf4j;
import com.ecommerce.product.event.StockUpdatedEvent;
import com.ecommerce.product.service.KafkaProducerService;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final OrderIntegrationService orderIntegrationService;
    private final KafkaProducerService kafkaProducerService;
    
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
                Product updatedProduct = productRepository.save(product);
                
                // Stok güncellemesini Kafka'ya gönder
                kafkaProducerService.sendStockUpdateEvent(new StockUpdatedEvent(
                    id,
                    updatedProduct.getStock(),
                    "UPDATED"
                ));
                
                return updatedProduct;
            })
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }
    
    /**
     * Ürün siparişi oluşturma
     * Circuit Breaker ile korunmuş order service entegrasyonu
     */
    public OrderResponse createProductOrder(Long productId, int quantity) {
        Product product = getProduct(productId);
        
        if (product.getStock() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product: " + productId);
        }
        
        // Order service'e istek at
        OrderRequest orderRequest = new OrderRequest(productId, quantity);
        OrderResponse orderResponse = orderIntegrationService.createOrder(orderRequest);
        
        // Sipariş başarılıysa stok güncelle
        if ("COMPLETED".equals(orderResponse.getStatus())) {
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        }
        
        return orderResponse;
    }
} 