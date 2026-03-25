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
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryRepository.save(category);
    }

    public Flux<CatalogProduct> getProducts() {
        return productRepository.findAll();
    }

    public Mono<CatalogProduct> createProduct(ProductRequest request) {
        CatalogProduct product = new CatalogProduct();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategoryId(request.getCategoryId());
        product.setUnitPrice(request.getUnitPrice());
        product.setReorderLevel(request.getReorderLevel());
        product.setActive(Boolean.TRUE);
        return productRepository.save(product);
    }
}
