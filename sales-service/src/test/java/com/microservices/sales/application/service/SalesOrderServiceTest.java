package com.microservices.sales.application.service;

import com.microservices.sales.application.dto.CatalogProductResponse;
import com.microservices.sales.application.dto.SalesItemRequest;
import com.microservices.sales.application.dto.SalesOrderRequest;
import com.microservices.sales.application.dto.SalesOrderResponse;
import com.microservices.sales.application.port.out.CatalogProductPort;
import com.microservices.sales.application.port.out.InventoryMovementPort;
import com.microservices.sales.domain.SalesOrder;
import com.microservices.sales.domain.SalesOrderItem;
import com.microservices.sales.infrastructure.repository.SalesOrderItemRepository;
import com.microservices.sales.infrastructure.repository.SalesOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalesOrderServiceTest {

    @Mock
    private SalesOrderRepository salesOrderRepository;

    @Mock
    private SalesOrderItemRepository salesOrderItemRepository;

    @Mock
    private CatalogProductPort catalogProductPort;

    @Mock
    private InventoryMovementPort inventoryMovementPort;

    @InjectMocks
    private SalesOrderService salesOrderService;

    @Test
    void createOrderCalculatesPriceFromCatalogAndPersistsConfirmedOrder() {
        UUID productId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        SalesItemRequest item = new SalesItemRequest();
        item.setProductId(productId);
        item.setQuantity(2);

        SalesOrderRequest request = new SalesOrderRequest();
        request.setReference("SO-3001");
        request.setSalesChannel("STORE");
        request.setItems(List.of(item));

        CatalogProductResponse product = new CatalogProductResponse();
        product.setId(productId);
        product.setUnitPrice(new BigDecimal("3200.00"));
        product.setActive(Boolean.TRUE);

        when(catalogProductPort.getProductById(productId)).thenReturn(Mono.just(product));
        when(inventoryMovementPort.registerMovements(any(), any())).thenReturn(Mono.empty());
        when(salesOrderRepository.save(any(SalesOrder.class))).thenAnswer(invocation -> {
            SalesOrder order = invocation.getArgument(0);
            order.setId(UUID.randomUUID());
            if (order.getCreatedAt() == null) {
                order.setCreatedAt(LocalDateTime.now());
            }
            return Mono.just(order);
        });
        when(salesOrderItemRepository.saveAll(any(Iterable.class))).thenAnswer(invocation -> {
            Iterable<SalesOrderItem> items = invocation.getArgument(0);
            return Flux.fromIterable(items);
        });

        StepVerifier.create(salesOrderService.createOrder(request, userId))
                .assertNext(response -> assertOrderResponse(response, productId))
                .verifyComplete();

        ArgumentCaptor<SalesOrder> orderCaptor = ArgumentCaptor.forClass(SalesOrder.class);
        verify(salesOrderRepository).save(orderCaptor.capture());
        verify(inventoryMovementPort).registerMovements(any(), org.mockito.ArgumentMatchers.eq(userId));
        org.junit.jupiter.api.Assertions.assertEquals(new BigDecimal("6400.00"),
                orderCaptor.getValue().getTotalAmount());
    }

    private void assertOrderResponse(SalesOrderResponse response, UUID productId) {
        org.junit.jupiter.api.Assertions.assertEquals("CONFIRMED", response.getStatus());
        org.junit.jupiter.api.Assertions.assertEquals(new BigDecimal("6400.00"), response.getTotalAmount());
        org.junit.jupiter.api.Assertions.assertEquals(1, response.getItems().size());
        org.junit.jupiter.api.Assertions.assertEquals(productId, response.getItems().getFirst().getProductId());
        org.junit.jupiter.api.Assertions.assertEquals(new BigDecimal("3200.00"), response.getItems().getFirst().getPrice());
        org.junit.jupiter.api.Assertions.assertEquals(new BigDecimal("6400.00"), response.getItems().getFirst().getSubtotal());
    }
}
