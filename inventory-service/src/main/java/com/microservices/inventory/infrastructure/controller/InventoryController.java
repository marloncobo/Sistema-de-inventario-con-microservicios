package com.microservices.inventory.infrastructure.controller;

import com.microservices.inventory.application.dto.MovementBatchRequest;
import com.microservices.inventory.application.dto.MovementRequest;
import com.microservices.inventory.application.service.InventoryService;
import com.microservices.inventory.domain.InventoryMovement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/movements")
    public Mono<InventoryMovement> registerMovement(
            @Valid @RequestBody MovementRequest request,
            @RequestHeader("X-User-Id") String userIdHeader) {
        UUID userId = UUID.fromString(userIdHeader);
        return inventoryService.registerMovement(request, userId);
    }

    @PostMapping("/movements/batch")
    public Flux<InventoryMovement> registerMovements(
            @Valid @RequestBody MovementBatchRequest request,
            @RequestHeader("X-User-Id") String userIdHeader) {
        UUID userId = UUID.fromString(userIdHeader);
        return inventoryService.registerMovements(request, userId);
    }
}
