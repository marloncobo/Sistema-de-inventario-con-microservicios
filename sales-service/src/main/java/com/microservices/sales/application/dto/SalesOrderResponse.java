package com.microservices.sales.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SalesOrderResponse {
    private final UUID id;
    private final String reference;
    private final String salesChannel;
    private final String status;
    private final BigDecimal totalAmount;
    private final LocalDateTime createdAt;
    private final List<SalesOrderItemResponse> items;

    public SalesOrderResponse(UUID id, String reference, String salesChannel, String status,
                              BigDecimal totalAmount, LocalDateTime createdAt, List<SalesOrderItemResponse> items) {
        this.id = id;
        this.reference = reference;
        this.salesChannel = salesChannel;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.items = items;
    }

    public UUID getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<SalesOrderItemResponse> getItems() {
        return items;
    }
}
