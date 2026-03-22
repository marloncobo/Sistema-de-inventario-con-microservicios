package com.microservices.sales.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesOrderRequest {
    @NotBlank
    private String reference;
    @NotBlank
    private String salesChannel;
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal totalAmount;
}
