package com.microservices.catalog.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductRequest {
    @NotBlank
    private String sku;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private UUID categoryId;
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal unitPrice;
    @NotNull
    @Min(0)
    private Integer reorderLevel;
}
