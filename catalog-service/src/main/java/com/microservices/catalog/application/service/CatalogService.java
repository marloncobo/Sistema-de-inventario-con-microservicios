package com.microservices.catalog.application.service;

import com.microservices.catalog.application.dto.CategoryRequest;
import com.microservices.catalog.application.dto.ProductRequest;
import com.microservices.catalog.domain.CatalogProduct;
import com.microservices.catalog.domain.Category;
import com.microservices.catalog.infrastructure.repository.CatalogProductRepository;
import com.microservices.catalog.infrastructure.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CategoryRepository categoryRepository;
    private final CatalogProductRepository productRepository;

    public Flux<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Mono<Category> createCategory(CategoryRequest request) {
        return categoryRepository.save(Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build());
    }

    public Flux<CatalogProduct> getProducts() {
        return productRepository.findAll();
    }

    public Mono<CatalogProduct> createProduct(ProductRequest request) {
        return productRepository.save(CatalogProduct.builder()
                .sku(request.getSku())
                .name(request.getName())
                .description(request.getDescription())
                .categoryId(request.getCategoryId())
                .unitPrice(request.getUnitPrice())
                .reorderLevel(request.getReorderLevel())
                .active(Boolean.TRUE)
                .build());
    }
}
