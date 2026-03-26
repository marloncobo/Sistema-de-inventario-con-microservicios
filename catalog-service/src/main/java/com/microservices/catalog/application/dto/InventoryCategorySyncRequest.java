package com.microservices.catalog.application.dto;

import java.util.UUID;

public class InventoryCategorySyncRequest {
    private final UUID id;
    private final String name;
    private final String description;

    public InventoryCategorySyncRequest(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
