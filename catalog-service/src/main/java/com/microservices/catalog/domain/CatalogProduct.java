package com.microservices.catalog.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("catalog_products")
public class CatalogProduct implements Persistable<UUID> {
    @Id
    private UUID id;
    private String sku;
    private String name;
    private String description;
    private UUID categoryId;
    private BigDecimal unitPrice;
    private Integer reorderLevel;
    private Boolean active;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
