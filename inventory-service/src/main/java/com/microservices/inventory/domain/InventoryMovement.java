package com.microservices.inventory.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("inventory_movements")
public class InventoryMovement implements Persistable<UUID> {
    @Id
    private UUID id;
    private UUID productId;
    private MovementType type;
    private Integer quantity;
    private UUID userId;
    private LocalDateTime createdAt;

    public InventoryMovement() {
    }

    public InventoryMovement(UUID id, UUID productId, MovementType type, Integer quantity, UUID userId, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.type = type;
        this.quantity = quantity;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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
