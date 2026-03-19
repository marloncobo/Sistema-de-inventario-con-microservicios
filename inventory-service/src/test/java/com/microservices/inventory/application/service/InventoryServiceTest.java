package com.microservices.inventory.application.service;

import com.microservices.inventory.application.dto.MovementRequest;
import com.microservices.inventory.domain.InventoryMovement;
import com.microservices.inventory.domain.MovementType;
import com.microservices.inventory.infrastructure.repository.InventoryMovementRepository;
import com.microservices.inventory.infrastructure.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryMovementRepository movementRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void registerMovementPersistsMovementWhenStockUpdateSucceeds() {
        UUID productId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        MovementRequest request = new MovementRequest();
        request.setProductId(productId);
        request.setType(MovementType.ENTRY);
        request.setQuantity(5);

        when(productRepository.incrementStock(productId, 5)).thenReturn(Mono.just(1));
        when(movementRepository.save(any(InventoryMovement.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(inventoryService.registerMovement(request, userId))
                .expectNextMatches(movement ->
                        movement.getProductId().equals(productId)
                                && movement.getUserId().equals(userId)
                                && movement.getQuantity() == 5
                                && movement.getType() == MovementType.ENTRY)
                .verifyComplete();
    }
}
