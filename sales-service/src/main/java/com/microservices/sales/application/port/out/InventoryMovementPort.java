package com.microservices.sales.application.port.out;

import com.microservices.sales.application.dto.InventoryMovementBatchRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface InventoryMovementPort {

    Mono<Void> registerMovements(InventoryMovementBatchRequest request, UUID userId);
}
