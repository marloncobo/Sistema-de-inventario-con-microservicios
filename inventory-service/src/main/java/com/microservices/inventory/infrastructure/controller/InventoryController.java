package com.microservices.inventory.infrastructure.controller;

import com.microservices.inventory.application.dto.MovementRequest;
import com.microservices.inventory.application.service.InventoryService;
import com.microservices.inventory.domain.InventoryMovement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/movements")
    public Mono<InventoryMovement> registerMovement(
            @Valid @RequestBody MovementRequest request,
            @RequestHeader("X-User-Id") String userIdHeader) {
        UUID userId = UUID.fromString(userIdHeader);
        return inventoryService.registerMovement(request, userId);
    }
}
