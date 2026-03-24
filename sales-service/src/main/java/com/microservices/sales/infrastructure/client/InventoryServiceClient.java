package com.microservices.sales.infrastructure.client;

import com.microservices.sales.application.dto.InventoryMovementBatchRequest;
import com.microservices.sales.application.exception.BusinessException;
import com.microservices.sales.application.port.out.InventoryMovementPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class InventoryServiceClient implements InventoryMovementPort {

    private final WebClient inventoryWebClient;

    public InventoryServiceClient(WebClient inventoryWebClient) {
        this.inventoryWebClient = inventoryWebClient;
    }

    @Override
    public Mono<Void> registerOutputMovements(InventoryMovementBatchRequest request, UUID userId) {
        return inventoryWebClient.post()
                .uri("/api/inventory/movements/batch")
                .header("X-User-Id", userId.toString())
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.CONFLICT::equals, response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("Inventory conflict")
                                .map(message -> new BusinessException(HttpStatus.CONFLICT, message)))
                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("Invalid inventory movement request")
                                .map(message -> new BusinessException(HttpStatus.BAD_REQUEST, message)))
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("Inventory service unavailable")
                                .map(message -> new BusinessException(HttpStatus.BAD_GATEWAY, message)))
                .toBodilessEntity()
                .then();
    }
}
