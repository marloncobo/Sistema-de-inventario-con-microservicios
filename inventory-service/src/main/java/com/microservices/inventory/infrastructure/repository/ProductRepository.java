package com.microservices.inventory.infrastructure.repository;

import com.microservices.inventory.domain.Product;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductRepository extends ReactiveCrudRepository<Product, UUID> {
    
    @Modifying
    @Query("UPDATE products SET current_stock = current_stock + :quantity WHERE id = :productId")
    Mono<Integer> incrementStock(UUID productId, Integer quantity);

    @Modifying
    @Query("UPDATE products SET current_stock = current_stock - :quantity WHERE id = :productId AND current_stock >= :quantity")
    Mono<Integer> decrementStock(UUID productId, Integer quantity);
}
