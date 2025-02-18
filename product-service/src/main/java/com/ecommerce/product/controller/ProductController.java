package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductCreateDTO;
import com.ecommerce.product.dto.ProductDTO;
import com.ecommerce.product.dto.ProductUpdateDTO;
import com.ecommerce.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product management APIs")
public class ProductController {
    
    private final ProductService productService;
    
    @Operation(summary = "Get all products", description = "Retrieve list of all products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
        @ApiResponse(responseCode = "204", description = "No products found")
    })
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        // ... mevcut kod
    }
    
    @Operation(summary = "Get product by ID", description = "Retrieve specific product by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved product"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(
        @Parameter(description = "Product ID", required = true)
        @PathVariable Long id
    ) {
        // ... mevcut kod
    }
    
    @Operation(summary = "Create new product", description = "Add new product to catalog")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(
        @RequestBody @Valid ProductCreateDTO productDTO
    ) {
        // ... mevcut kod
    }
    
    @Operation(summary = "Update product", description = "Update existing product details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product successfully updated"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(
        @Parameter(description = "Product ID", required = true)
        @PathVariable Long id,
        @RequestBody @Valid ProductUpdateDTO productDTO
    ) {
        // ... mevcut kod
    }
    
    @Operation(summary = "Delete product", description = "Remove product from catalog")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(
        @Parameter(description = "Product ID", required = true)
        @PathVariable Long id
    ) {
        // ... mevcut kod
    }
} 