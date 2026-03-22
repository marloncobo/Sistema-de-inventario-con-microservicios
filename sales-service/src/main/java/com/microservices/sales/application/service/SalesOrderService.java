package com.microservices.sales.application.service;

import com.microservices.sales.application.dto.SalesOrderRequest;
import com.microservices.sales.domain.SalesOrder;
import com.microservices.sales.infrastructure.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;

    public Flux<SalesOrder> getOrders() {
        return salesOrderRepository.findAll();
    }

    public Mono<SalesOrder> createOrder(SalesOrderRequest request) {
        return salesOrderRepository.save(SalesOrder.builder()
                .reference(request.getReference())
                .salesChannel(request.getSalesChannel())
                .status("CONFIRMED")
                .totalAmount(request.getTotalAmount())
                .createdAt(LocalDateTime.now())
                .build());
    }
}
