package com.microservices.sales.infrastructure.controller;

import com.microservices.sales.application.dto.SalesOrderRequest;
import com.microservices.sales.application.service.SalesOrderService;
import com.microservices.sales.domain.SalesOrder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/sales/orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @GetMapping
    public Flux<SalesOrder> getOrders() {
        return salesOrderService.getOrders();
    }

    @PostMapping
    public Mono<SalesOrder> createOrder(@Valid @RequestBody SalesOrderRequest request) {
        return salesOrderService.createOrder(request);
    }
}
