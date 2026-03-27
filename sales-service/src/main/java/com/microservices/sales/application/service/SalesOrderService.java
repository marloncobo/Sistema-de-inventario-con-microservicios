package com.microservices.sales.application.service;

import com.microservices.sales.application.dto.CatalogProductResponse;
import com.microservices.sales.application.dto.InventoryMovementBatchRequest;
import com.microservices.sales.application.dto.InventoryMovementRequest;
import com.microservices.sales.application.dto.ResolvedSalesItem;
import com.microservices.sales.application.dto.SalesItemRequest;
import com.microservices.sales.application.dto.SalesOrderItemResponse;
import com.microservices.sales.application.dto.SalesOrderRequest;
import com.microservices.sales.application.dto.SalesOrderResponse;
import com.microservices.sales.application.exception.BusinessException;
import com.microservices.sales.application.port.out.CatalogProductPort;
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

    private static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    private static final String REFERENCE_PREFIX = "SO-";

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final CatalogProductPort catalogProductPort;
    private final InventoryMovementPort inventoryMovementPort;

    public SalesOrderService(SalesOrderRepository salesOrderRepository,
                             SalesOrderItemRepository salesOrderItemRepository,
                             CatalogProductPort catalogProductPort,
                             InventoryMovementPort inventoryMovementPort) {
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.catalogProductPort = catalogProductPort;
        this.inventoryMovementPort = inventoryMovementPort;
    }

    public Flux<SalesOrderResponse> getOrders() {
        return salesOrderRepository.findAll().flatMap(this::buildResponse);
    }

    @Transactional
    public Mono<SalesOrderResponse> createOrder(SalesOrderRequest request, UUID userId) {
        return resolveItems(request.getItems())
                .collectList()
                .flatMap(resolvedItems -> registerInventoryMovements(resolvedItems, "EXIT", userId)
                        .then(saveConfirmedOrder(request, resolvedItems)
                                .onErrorResume(error -> registerInventoryMovements(resolvedItems, "ENTRY", userId)
                                        .onErrorResume(compensationError -> Mono.empty())
                                        .then(Mono.error(error)))));
    }

    private Flux<ResolvedSalesItem> resolveItems(List<SalesItemRequest> items) {
        return Flux.fromIterable(items)
                .flatMap(this::resolveItem);
    }

    private Mono<ResolvedSalesItem> resolveItem(SalesItemRequest item) {
        return catalogProductPort.getProductById(item.getProductId())
                .flatMap(product -> toResolvedItem(item, product));
    }

    private Mono<ResolvedSalesItem> toResolvedItem(SalesItemRequest item, CatalogProductResponse product) {
        if (Boolean.FALSE.equals(product.getActive())) {
            return Mono.error(new BusinessException(HttpStatus.BAD_REQUEST,
                    "Inactive products cannot be sold"));
        }
        return Mono.just(new ResolvedSalesItem(item.getProductId(), item.getQuantity(), product.getUnitPrice()));
    }

    private Mono<SalesOrderResponse> saveConfirmedOrder(SalesOrderRequest request, List<ResolvedSalesItem> items) {
        return generateUniqueReference()
                .flatMap(reference -> {
                    SalesOrder salesOrder = new SalesOrder();
                    salesOrder.setReference(reference);
                    salesOrder.setSalesChannel(request.getSalesChannel());
                    salesOrder.setStatus(ORDER_STATUS_CONFIRMED);
                    salesOrder.setTotalAmount(calculateTotal(items));
                    salesOrder.setCreatedAt(LocalDateTime.now());

                    return salesOrderRepository.save(salesOrder);
                })
                .flatMap(savedOrder -> saveItems(savedOrder.getId(), items)
                        .collectList()
                        .flatMap(savedItems -> toResponse(savedOrder, savedItems)));
    }

    private Mono<String> generateUniqueReference() {
        String reference = REFERENCE_PREFIX + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return salesOrderRepository.existsByReference(reference)
                .flatMap(exists -> exists ? generateUniqueReference() : Mono.just(reference));
    }

    private Flux<SalesOrderItem> saveItems(UUID salesOrderId, List<ResolvedSalesItem> items) {
        List<SalesOrderItem> orderItems = items.stream()
                .map(item -> new SalesOrderItem(null, salesOrderId, item.getProductId(), item.getQuantity(), item.getPrice()))
                .toList();
        return salesOrderItemRepository.saveAll(orderItems);
    }

    private Mono<Void> registerInventoryMovements(List<ResolvedSalesItem> items, String movementType, UUID userId) {
        InventoryMovementBatchRequest batchRequest = new InventoryMovementBatchRequest(
                items.stream()
                        .map(item -> new InventoryMovementRequest(item.getProductId(), movementType, item.getQuantity()))
                        .toList());

        return inventoryMovementPort.registerMovements(batchRequest, userId);
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

    private BigDecimal calculateSubtotal(ResolvedSalesItem item) {
        return item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    private BigDecimal calculateTotal(List<ResolvedSalesItem> items) {
        return items.stream()
                .map(this::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
