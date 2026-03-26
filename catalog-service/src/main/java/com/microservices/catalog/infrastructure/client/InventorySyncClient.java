package com.microservices.catalog.infrastructure.client;

import com.microservices.catalog.application.dto.InventoryCategorySyncRequest;
import com.microservices.catalog.application.dto.InventoryProductSyncRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class InventorySyncClient {

    private final WebClient inventoryWebClient;

    public InventorySyncClient(WebClient inventoryWebClient) {
        this.inventoryWebClient = inventoryWebClient;
    }

    public Mono<Void> syncCategory(InventoryCategorySyncRequest request) {
        return inventoryWebClient.post()
                .uri("/internal/sync/categories")
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.isError(), response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("Failed to synchronize category with inventory")
                        .map(message -> new ResponseStatusException(HttpStatus.BAD_GATEWAY, message)))
                .toBodilessEntity()
                .then()
                .onErrorMap(error -> error instanceof ResponseStatusException
                        ? error
                        : new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                        "Inventory synchronization failed", error));
    }

    public Mono<Void> syncProduct(InventoryProductSyncRequest request) {
        return inventoryWebClient.post()
                .uri("/internal/sync/products")
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.isError(), response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("Failed to synchronize product with inventory")
                        .map(message -> new ResponseStatusException(HttpStatus.BAD_GATEWAY, message)))
                .toBodilessEntity()
                .then()
                .onErrorMap(error -> error instanceof ResponseStatusException
                        ? error
                        : new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                        "Inventory synchronization failed", error));
    }
}
