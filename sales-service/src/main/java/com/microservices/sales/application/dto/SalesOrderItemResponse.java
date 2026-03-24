package com.microservices.sales.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class SalesOrderItemResponse {
    private final UUID productId;
    private final Integer quantity;
    private final BigDecimal price;
    private final BigDecimal subtotal;

    public SalesOrderItemResponse(UUID productId, Integer quantity, BigDecimal price, BigDecimal subtotal) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
