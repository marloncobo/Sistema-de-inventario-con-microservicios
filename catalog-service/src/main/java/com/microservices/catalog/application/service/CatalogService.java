package com.microservices.catalog.application.service;

import com.microservices.catalog.application.dto.CategoryRequest;
import com.microservices.catalog.application.dto.InventoryCategorySyncRequest;
import com.microservices.catalog.application.dto.InventoryProductSyncRequest;
import com.microservices.catalog.application.dto.ProductRequest;
import com.microservices.catalog.domain.CatalogProduct;
import com.microservices.catalog.domain.Category;
import com.microservices.catalog.infrastructure.client.InventorySyncClient;
import com.microservices.catalog.infrastructure.repository.CatalogProductRepository;
import com.microservices.catalog.infrastructure.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CategoryRepository categoryRepository;
    private final CatalogProductRepository productRepository;
    private final InventorySyncClient inventorySyncClient;

    public Flux<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Mono<Category> createCategory(CategoryRequest request) {
        return categoryRepository.existsByName(request.getName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,
                                "Category name already exists"));
                    }

                    Category category = new Category();
                    category.setName(request.getName());
                    category.setDescription(request.getDescription());

                    return categoryRepository.save(category)
                            .flatMap(savedCategory -> inventorySyncClient.syncCategory(
                                            new InventoryCategorySyncRequest(
                                                    savedCategory.getId(),
                                                    savedCategory.getName(),
                                                    savedCategory.getDescription()))
                                    .thenReturn(savedCategory)
                                    .onErrorResume(error -> categoryRepository.deleteById(savedCategory.getId())
                                            .then(Mono.error(error))));
                });
    }

    public Flux<CatalogProduct> getProducts() {
        return productRepository.findAll();
    }

    public Mono<CatalogProduct> getProductById(UUID productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Product not found")));
    }

    public Mono<CatalogProduct> createProduct(ProductRequest request) {
        return categoryRepository.findById(request.getCategoryId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Category does not exist")))
                .then(productRepository.existsBySku(request.getSku()))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,
                                "Product SKU already exists"));
                    }

                    CatalogProduct product = new CatalogProduct();
                    product.setSku(request.getSku());
                    product.setName(request.getName());
                    product.setDescription(request.getDescription());
                    product.setCategoryId(request.getCategoryId());
                    product.setUnitPrice(request.getUnitPrice());
                    product.setReorderLevel(request.getReorderLevel());
                    product.setActive(Boolean.TRUE);

                    return productRepository.save(product)
                            .flatMap(savedProduct -> inventorySyncClient.syncProduct(
                                            new InventoryProductSyncRequest(
                                                    savedProduct.getId(),
                                                    savedProduct.getSku(),
                                                    savedProduct.getName(),
                                                    savedProduct.getDescription(),
                                                    savedProduct.getCategoryId(),
                                                    savedProduct.getUnitPrice(),
                                                    savedProduct.getReorderLevel(),
                                                    savedProduct.getActive()))
                                    .thenReturn(savedProduct)
                                    .onErrorResume(error -> productRepository.deleteById(savedProduct.getId())
                                            .then(Mono.error(error))));
                });
    }
}
