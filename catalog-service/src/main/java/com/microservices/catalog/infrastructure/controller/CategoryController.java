package com.microservices.catalog.infrastructure.controller;

import com.microservices.catalog.application.dto.CategoryRequest;
import com.microservices.catalog.application.service.CatalogService;
import com.microservices.catalog.domain.Category;
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
@RequestMapping("/api/catalog/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CatalogService catalogService;

    @GetMapping
    public Flux<Category> getCategories() {
        return catalogService.getCategories();
    }

    @PostMapping
    public Mono<Category> createCategory(@Valid @RequestBody CategoryRequest request) {
        return catalogService.createCategory(request);
    }
}
