package com.microservices.inventory.infrastructure.controller;

import com.microservices.inventory.application.service.InventoryService;
import com.microservices.inventory.domain.InventoryMovement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final InventoryService inventoryService;

    @GetMapping("/movements")
    public Flux<InventoryMovement> getMovements() {
        return inventoryService.getMovements();
    }
}
