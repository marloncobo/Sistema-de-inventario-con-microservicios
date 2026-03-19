package com.microservices.inventory.application.service;

import com.microservices.inventory.application.dto.MovementRequest;
import com.microservices.inventory.domain.InventoryMovement;
import com.microservices.inventory.domain.MovementType;
import com.microservices.inventory.domain.Product;
import com.microservices.inventory.infrastructure.repository.InventoryMovementRepository;
import com.microservices.inventory.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;
    private final InventoryMovementRepository movementRepository;

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Mono<InventoryMovement> registerMovement(MovementRequest request, UUID userId) {
        if (request.getQuantity() <= 0) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero"));
        }

        Mono<Integer> updateStock = request.getType() == MovementType.ENTRY
                ? productRepository.incrementStock(request.getProductId(), request.getQuantity())
                : productRepository.decrementStock(request.getProductId(), request.getQuantity());

        return updateStock.flatMap(updatedRows -> {
            if (updatedRows == 0) {
                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient stock or product not found"));
            }

            InventoryMovement movement = InventoryMovement.builder()
                    .productId(request.getProductId())
                    .type(request.getType())
                    .quantity(request.getQuantity())
                    .userId(userId)
                    .createdAt(LocalDateTime.now())
                    .build();

            return movementRepository.save(movement);
        });
    }

    public Flux<InventoryMovement> getMovements() {
        return movementRepository.findAll();
    }
}
