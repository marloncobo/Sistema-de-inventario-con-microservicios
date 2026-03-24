package com.microservices.inventory.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("products")
public class Product implements Persistable<UUID> {
    @Id
    private UUID id;
    private String name;
    private UUID categoryId;
    private Integer currentStock;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
