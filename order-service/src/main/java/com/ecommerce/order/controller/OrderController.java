package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderCreateDTO;
import com.ecommerce.order.dto.OrderDTO;
import com.ecommerce.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order management APIs")
public class OrderController {
    
    private final OrderService orderService;
    
    @Operation(summary = "Create new order", description = "Place new order for products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
        @RequestBody @Valid OrderCreateDTO orderDTO
    ) {
        // ... mevcut kod
    }
    
    @Operation(summary = "Get order by ID", description = "Retrieve specific order details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved order"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(
        @Parameter(description = "Order ID", required = true)
        @PathVariable Long id
    ) {
        // ... mevcut kod
    }
    
    @Operation(summary = "Get user orders", description = "Retrieve all orders for specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved orders"),
        @ApiResponse(responseCode = "204", description = "No orders found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(
        @Parameter(description = "User ID", required = true)
        @PathVariable Long userId
    ) {
        // ... mevcut kod
    }
} 