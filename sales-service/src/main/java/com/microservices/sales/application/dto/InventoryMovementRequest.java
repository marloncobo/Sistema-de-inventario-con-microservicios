package com.microservices.sales.application.dto;

import java.util.UUID;

public class InventoryMovementRequest {
    private UUID productId;
    private String type;
    private Integer quantity;

    public InventoryMovementRequest() {
    }

    public InventoryMovementRequest(UUID productId, String type, Integer quantity) {
        this.productId = productId;
        this.type = type;
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
