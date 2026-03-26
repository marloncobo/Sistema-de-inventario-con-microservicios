package com.microservices.sales.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ResolvedSalesItem {
    private final UUID productId;
    private final Integer quantity;
    private final BigDecimal price;

    public ResolvedSalesItem(UUID productId, Integer quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public UUID getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
