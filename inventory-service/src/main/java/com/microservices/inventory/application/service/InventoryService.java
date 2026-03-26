package com.microservices.inventory.application.service;

import com.microservices.inventory.application.dto.MovementBatchRequest;
import com.microservices.inventory.application.dto.MovementRequest;
import com.microservices.inventory.application.dto.ProductSyncRequest;
import com.microservices.inventory.domain.InventoryMovement;
import com.microservices.inventory.domain.MovementType;
import com.microservices.inventory.domain.Product;
import com.microservices.inventory.infrastructure.repository.InventoryMovementRepository;
import com.microservices.inventory.infrastructure.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final InventoryMovementRepository movementRepository;
    private final DatabaseClient databaseClient;

    public InventoryService(ProductRepository productRepository,
                            InventoryMovementRepository movementRepository,
                            DatabaseClient databaseClient) {
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
        this.databaseClient = databaseClient;
    }

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Mono<Product> syncProduct(ProductSyncRequest request) {
        DatabaseClient.GenericExecuteSpec statement = databaseClient.sql("""
                        INSERT INTO products (id, sku, name, description, category_id, unit_price, reorder_level, active, current_stock)
                        VALUES (:id, :sku, :name, :description, :categoryId, :unitPrice, :reorderLevel, :active, 0)
                        ON CONFLICT (id) DO UPDATE
                        SET sku = EXCLUDED.sku,
                            name = EXCLUDED.name,
                            description = EXCLUDED.description,
                            category_id = EXCLUDED.category_id,
                            unit_price = EXCLUDED.unit_price,
                            reorder_level = EXCLUDED.reorder_level,
                            active = EXCLUDED.active
                        """)
                .bind("id", request.getId())
                .bind("sku", request.getSku())
                .bind("name", request.getName())
                .bind("categoryId", request.getCategoryId())
                .bind("unitPrice", request.getUnitPrice())
                .bind("reorderLevel", request.getReorderLevel())
                .bind("active", request.getActive());

        statement = request.getDescription() == null
                ? statement.bindNull("description", String.class)
                : statement.bind("description", request.getDescription());

        return statement.fetch()
                .rowsUpdated()
                .then(productRepository.findById(request.getId()));
    }

    @Transactional
    public Mono<InventoryMovement> registerMovement(MovementRequest request, UUID userId) {
        return registerMovementInternal(request, userId);
    }

    @Transactional
    public Flux<InventoryMovement> registerMovements(MovementBatchRequest request, UUID userId) {
        return Flux.fromIterable(request.getMovements())
                .concatMap(movement -> registerMovementInternal(movement, userId));
    }

    private Mono<InventoryMovement> registerMovementInternal(MovementRequest request, UUID userId) {
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

            InventoryMovement movement = new InventoryMovement();
            movement.setProductId(request.getProductId());
            movement.setType(request.getType());
            movement.setQuantity(request.getQuantity());
            movement.setUserId(userId);
            movement.setCreatedAt(LocalDateTime.now());

            return movementRepository.save(movement);
        });
    }

    public Flux<InventoryMovement> getMovements() {
        return movementRepository.findAll();
    }
}
