package com.microservices.inventory.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("inventory_movements")
public class InventoryMovement implements Persistable<UUID> {
    @Id
    private UUID id;
    private UUID productId;
    private MovementType type;
    private Integer quantity;
    private UUID userId;
    private LocalDateTime createdAt;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
