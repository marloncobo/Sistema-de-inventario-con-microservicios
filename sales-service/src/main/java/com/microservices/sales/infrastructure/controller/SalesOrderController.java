package com.microservices.sales.infrastructure.controller;

import com.microservices.sales.application.dto.SalesOrderRequest;
import com.microservices.sales.application.dto.SalesOrderResponse;
import com.microservices.sales.application.service.SalesOrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/sales/orders")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    @GetMapping
    public Flux<SalesOrderResponse> getOrders() {
        return salesOrderService.getOrders();
    }

    @PostMapping
    public Mono<SalesOrderResponse> createOrder(
            @Valid @RequestBody SalesOrderRequest request,
            @RequestHeader("X-User-Id") String userIdHeader) {
        return salesOrderService.createOrder(request, UUID.fromString(userIdHeader));
    }
}
