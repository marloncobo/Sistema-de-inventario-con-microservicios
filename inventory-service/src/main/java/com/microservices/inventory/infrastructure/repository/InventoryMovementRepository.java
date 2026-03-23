package com.microservices.inventory.infrastructure.repository;

import com.microservices.inventory.domain.InventoryMovement;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface InventoryMovementRepository extends ReactiveCrudRepository<InventoryMovement, UUID> {
}
