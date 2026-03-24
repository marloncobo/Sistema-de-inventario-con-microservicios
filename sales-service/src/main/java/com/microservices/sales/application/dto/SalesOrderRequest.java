package com.microservices.sales.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class SalesOrderRequest {
    @NotBlank
    private String reference;
    @NotBlank
    private String salesChannel;
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal totalAmount;
    @NotEmpty
    private List<@NotNull SalesItemRequest> items;

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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<SalesItemRequest> getItems() {
        return items;
    }

    public void setItems(List<SalesItemRequest> items) {
        this.items = items;
    }
}
