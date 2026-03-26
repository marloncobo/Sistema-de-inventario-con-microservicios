package com.microservices.inventory.infrastructure.controller;

import com.microservices.inventory.application.dto.CategorySyncRequest;
import com.microservices.inventory.application.dto.ProductSyncRequest;
import com.microservices.inventory.application.service.CategoryService;
import com.microservices.inventory.application.service.InventoryService;
import com.microservices.inventory.domain.Category;
import com.microservices.inventory.domain.Product;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/internal/sync")
public class SyncController {

    private final CategoryService categoryService;
    private final InventoryService inventoryService;

    public SyncController(CategoryService categoryService, InventoryService inventoryService) {
        this.categoryService = categoryService;
        this.inventoryService = inventoryService;
    }

    @PostMapping("/categories")
    public Mono<Category> syncCategory(@Valid @RequestBody CategorySyncRequest request) {
        return categoryService.syncCategory(request);
    }

    @PostMapping("/products")
    public Mono<Product> syncProduct(@Valid @RequestBody ProductSyncRequest request) {
        return inventoryService.syncProduct(request);
    }
}
