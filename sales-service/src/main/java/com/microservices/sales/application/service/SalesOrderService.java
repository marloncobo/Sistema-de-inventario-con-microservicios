package com.microservices.sales.application.service;

import com.microservices.sales.application.dto.InventoryMovementBatchRequest;
import com.microservices.sales.application.dto.InventoryMovementRequest;
import com.microservices.sales.application.dto.SalesItemRequest;
import com.microservices.sales.application.dto.SalesOrderItemResponse;
import com.microservices.sales.application.dto.SalesOrderRequest;
import com.microservices.sales.application.dto.SalesOrderResponse;
import com.microservices.sales.application.exception.BusinessException;
import com.microservices.sales.application.port.out.InventoryMovementPort;
import com.microservices.sales.domain.SalesOrder;
import com.microservices.sales.domain.SalesOrderItem;
import com.microservices.sales.infrastructure.repository.SalesOrderItemRepository;
import com.microservices.sales.infrastructure.repository.SalesOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SalesOrderService {

    private static final String ORDER_STATUS_PENDING = "PENDING_INVENTORY";
    private static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    private static final String ORDER_STATUS_REJECTED = "REJECTED";

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final InventoryMovementPort inventoryMovementPort;

    public SalesOrderService(SalesOrderRepository salesOrderRepository,
                             SalesOrderItemRepository salesOrderItemRepository,
                             InventoryMovementPort inventoryMovementPort) {
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.inventoryMovementPort = inventoryMovementPort;
    }

    public Flux<SalesOrderResponse> getOrders() {
        return salesOrderRepository.findAll().flatMap(this::buildResponse);
    }

    @Transactional
    public Mono<SalesOrderResponse> createOrder(SalesOrderRequest request, UUID userId) {
        validateRequest(request);

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setReference(request.getReference());
        salesOrder.setSalesChannel(request.getSalesChannel());
        salesOrder.setStatus(ORDER_STATUS_PENDING);
        salesOrder.setTotalAmount(request.getTotalAmount());
        salesOrder.setCreatedAt(LocalDateTime.now());

        return salesOrderRepository.save(salesOrder)
                .flatMap(savedOrder -> saveItems(savedOrder.getId(), request.getItems())
                        .collectList()
                        .flatMap(savedItems -> registerInventoryOutput(request.getItems(), userId)
                                .then(updateOrderStatus(savedOrder, ORDER_STATUS_CONFIRMED))
                                .flatMap(confirmedOrder -> toResponse(confirmedOrder, savedItems))
                                .onErrorResume(error -> updateOrderStatus(savedOrder, ORDER_STATUS_REJECTED)
                                        .then(Mono.error(error)))));
    }

    private void validateRequest(SalesOrderRequest request) {
        BigDecimal calculatedTotal = request.getItems().stream()
                .map(this::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (calculatedTotal.compareTo(request.getTotalAmount()) != 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "The totalAmount does not match the sum of sales items");
        }
    }

    private Flux<SalesOrderItem> saveItems(UUID salesOrderId, List<SalesItemRequest> items) {
        List<SalesOrderItem> orderItems = items.stream()
                .map(item -> new SalesOrderItem(null, salesOrderId, item.getProductId(), item.getQuantity(), item.getPrice()))
                .toList();
        return salesOrderItemRepository.saveAll(orderItems);
    }

    private Mono<Void> registerInventoryOutput(List<SalesItemRequest> items, UUID userId) {
        InventoryMovementBatchRequest batchRequest = new InventoryMovementBatchRequest(
                items.stream()
                        .map(item -> new InventoryMovementRequest(item.getProductId(), "EXIT", item.getQuantity()))
                        .toList());

        return inventoryMovementPort.registerOutputMovements(batchRequest, userId);
    }

    private Mono<SalesOrder> updateOrderStatus(SalesOrder salesOrder, String status) {
        salesOrder.setStatus(status);
        return salesOrderRepository.save(salesOrder);
    }

    private Mono<SalesOrderResponse> buildResponse(SalesOrder order) {
        return salesOrderItemRepository.findBySalesOrderId(order.getId())
                .collectList()
                .flatMap(items -> toResponse(order, items));
    }

    private Mono<SalesOrderResponse> toResponse(SalesOrder order, List<SalesOrderItem> items) {
        return Mono.just(new SalesOrderResponse(
                order.getId(),
                order.getReference(),
                order.getSalesChannel(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                items.stream()
                        .map(item -> new SalesOrderItemResponse(
                                item.getProductId(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))))
                        .toList()));
    }

    private BigDecimal calculateSubtotal(SalesItemRequest item) {
        return item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}
