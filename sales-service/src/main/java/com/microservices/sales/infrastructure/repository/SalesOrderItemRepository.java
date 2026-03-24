package com.microservices.sales.infrastructure.repository;

import com.microservices.sales.domain.SalesOrderItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface SalesOrderItemRepository extends ReactiveCrudRepository<SalesOrderItem, UUID> {

    Flux<SalesOrderItem> findBySalesOrderId(UUID salesOrderId);
}
