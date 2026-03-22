package com.microservices.catalog.infrastructure.controller;

import com.microservices.catalog.application.dto.ProductRequest;
import com.microservices.catalog.application.service.CatalogService;
import com.microservices.catalog.domain.CatalogProduct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/catalog/products")
@RequiredArgsConstructor
public class ProductController {

    private final CatalogService catalogService;

    @GetMapping
    public Flux<CatalogProduct> getProducts() {
        return catalogService.getProducts();
    }

    @PostMapping
    public Mono<CatalogProduct> createProduct(@Valid @RequestBody ProductRequest request) {
        return catalogService.createProduct(request);
    }
}
