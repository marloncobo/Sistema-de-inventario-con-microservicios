package com.microservices.sales.application.port.out;

import com.microservices.sales.application.dto.CatalogProductResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CatalogProductPort {

    Mono<CatalogProductResponse> getProductById(UUID productId);
}
