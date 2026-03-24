package com.microservices.sales.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("sales_orders")
public class SalesOrder implements Persistable<UUID> {
    @Id
    private UUID id;
    private String reference;
    private String salesChannel;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    public SalesOrder() {
    }

    public SalesOrder(UUID id, String reference, String salesChannel, String status, BigDecimal totalAmount, LocalDateTime createdAt) {
        this.id = id;
        this.reference = reference;
        this.salesChannel = salesChannel;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
