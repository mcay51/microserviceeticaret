package com.ecommerce.product.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Product Service API",
        version = "1.0",
        description = "Product service endpoints"
    )
)
public class OpenApiConfig {
} 