package com.microservices.inventory.infrastructure.controller;

import com.microservices.inventory.application.service.CategoryService;
import com.microservices.inventory.domain.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Flux<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
