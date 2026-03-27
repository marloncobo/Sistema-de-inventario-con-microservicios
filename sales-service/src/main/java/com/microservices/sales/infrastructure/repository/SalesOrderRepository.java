package com.microservices.sales.infrastructure.repository;

import com.microservices.sales.domain.SalesOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SalesOrderRepository extends ReactiveCrudRepository<SalesOrder, UUID> {
    Mono<Boolean> existsByReference(String reference);
}
