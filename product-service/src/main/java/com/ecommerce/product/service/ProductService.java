package com.ecommerce.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.ecommerce.product.domain.Product;
import com.ecommerce.product.dto.ProductCreateDTO;
import com.ecommerce.product.dto.ProductDTO;
import com.ecommerce.product.dto.ProductUpdateDTO;
import com.ecommerce.product.exception.ProductNotFoundException;
import com.ecommerce.product.exception.InsufficientStockException;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.dto.OrderRequest;
import com.ecommerce.product.dto.OrderResponse;
import com.ecommerce.product.service.OrderIntegrationService;
import lombok.extern.slf4j.Slf4j;
import com.ecommerce.product.event.StockUpdatedEvent;
import com.ecommerce.product.service.KafkaProducerService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final OrderIntegrationService orderIntegrationService;
    private final KafkaProducerService kafkaProducerService;
    
    public ProductDTO createProduct(ProductCreateDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        
        return convertToDTO(productRepository.save(product));
    }
    
    @Transactional(readOnly = true)
    public ProductDTO getProduct(Long id) {
        return convertToDTO(productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id)));
    }
    
    public ProductDTO updateProduct(Long id, ProductUpdateDTO dto) {
        return productRepository.findById(id)
            .map(product -> {
                if (dto.getName() != null) {
                    product.setName(dto.getName());
                }
                if (dto.getPrice() != null) {
                    product.setPrice(dto.getPrice());
                }
                if (dto.getStock() != null) {
                    product.setStock(dto.getStock());
                }
                return convertToDTO(productRepository.save(product));
            })
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }
    
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
    
    public Product updateStock(Long id, int quantity) {
        return productRepository.findById(id)
            .map(product -> {
                if (product.getStock() < quantity) {
                    throw new InsufficientStockException("Insufficient stock for product: " + id);
                }
                product.setStock(product.getStock() - quantity);
                Product updatedProduct = productRepository.save(product);
                
                // Metod ismini düzelttik
                kafkaProducerService.sendStockUpdate(new StockUpdatedEvent(
                    id,
                    updatedProduct.getStock(),
                    "Stock updated successfully"
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
    
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public Product reserveStock(Long productId, Integer quantity) {
        return productRepository.findById(productId)
            .map(product -> {
                if (product.getStock() < quantity) {
                    throw new InsufficientStockException("Insufficient stock for product: " + productId);
                }
                product.setStock(product.getStock() - quantity);
                return productRepository.save(product);
            })
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }
    
    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getStock()
        );
    }
} 