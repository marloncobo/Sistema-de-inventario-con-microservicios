package com.microservices.catalog.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class InventoryProductSyncRequest {
    private final UUID id;
    private final String sku;
    private final String name;
    private final String description;
    private final UUID categoryId;
    private final BigDecimal unitPrice;
    private final Integer reorderLevel;
    private final Boolean active;

    public InventoryProductSyncRequest(UUID id,
                                       String sku,
                                       String name,
                                       String description,
                                       UUID categoryId,
                                       BigDecimal unitPrice,
                                       Integer reorderLevel,
                                       Boolean active) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.unitPrice = unitPrice;
        this.reorderLevel = reorderLevel;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public Boolean getActive() {
        return active;
    }
}
