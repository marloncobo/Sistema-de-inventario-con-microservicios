package com.microservices.inventory.infrastructure.controller;

import com.microservices.inventory.application.service.InventoryService;
import com.microservices.inventory.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final InventoryService inventoryService;

    @GetMapping
    public Flux<Product> getAllProducts() {
        return inventoryService.getAllProducts();
    }
}
