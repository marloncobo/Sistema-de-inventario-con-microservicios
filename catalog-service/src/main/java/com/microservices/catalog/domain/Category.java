package com.microservices.catalog.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("categories")
public class Category implements Persistable<UUID> {
    @Id
    private UUID id;
    private String name;
    private String description;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
