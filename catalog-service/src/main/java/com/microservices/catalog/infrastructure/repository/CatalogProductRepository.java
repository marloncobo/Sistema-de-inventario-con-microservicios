package com.microservices.catalog.infrastructure.repository;

import com.microservices.catalog.domain.CatalogProduct;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface CatalogProductRepository extends ReactiveCrudRepository<CatalogProduct, UUID> {

    reactor.core.publisher.Mono<Boolean> existsBySku(String sku);
}
