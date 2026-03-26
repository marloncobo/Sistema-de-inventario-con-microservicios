package com.microservices.sales.infrastructure.client;

import com.microservices.sales.application.dto.CatalogProductResponse;
import com.microservices.sales.application.exception.BusinessException;
import com.microservices.sales.application.port.out.CatalogProductPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CatalogServiceClient implements CatalogProductPort {

    private final WebClient catalogWebClient;

    public CatalogServiceClient(WebClient catalogWebClient) {
        this.catalogWebClient = catalogWebClient;
    }

    @Override
    public Mono<CatalogProductResponse> getProductById(UUID productId) {
        return catalogWebClient.get()
                .uri("/api/catalog/products/{productId}", productId)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.just(
                        new BusinessException(HttpStatus.BAD_REQUEST, "Product does not exist in catalog")))
                .onStatus(status -> status.isError(), response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("Catalog service unavailable")
                        .map(message -> new BusinessException(HttpStatus.BAD_GATEWAY, message)))
                .bodyToMono(CatalogProductResponse.class);
    }
}
