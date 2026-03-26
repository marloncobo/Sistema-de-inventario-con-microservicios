package com.microservices.catalog.application.service;

import com.microservices.catalog.application.dto.InventoryProductSyncRequest;
import com.microservices.catalog.application.dto.ProductRequest;
import com.microservices.catalog.domain.CatalogProduct;
import com.microservices.catalog.domain.Category;
import com.microservices.catalog.infrastructure.client.InventorySyncClient;
import com.microservices.catalog.infrastructure.repository.CatalogProductRepository;
import com.microservices.catalog.infrastructure.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CatalogProductRepository productRepository;

    @Mock
    private InventorySyncClient inventorySyncClient;

    @InjectMocks
    private CatalogService catalogService;

    @Test
    void createProductValidatesCategoryAndSynchronizesInventory() {
        UUID categoryId = UUID.randomUUID();

        ProductRequest request = new ProductRequest();
        request.setSku("SKU-CHA-010");
        request.setName("Office Chair");
        request.setDescription("Ergonomic chair");
        request.setCategoryId(categoryId);
        request.setUnitPrice(new BigDecimal("450.00"));
        request.setReorderLevel(6);

        Category category = new Category(categoryId, "Office", "Office supplies");
        when(categoryRepository.findById(categoryId)).thenReturn(Mono.just(category));
        when(productRepository.existsBySku("SKU-CHA-010")).thenReturn(Mono.just(false));
        when(productRepository.save(any(CatalogProduct.class))).thenAnswer(invocation -> {
            CatalogProduct product = invocation.getArgument(0);
            product.setId(UUID.randomUUID());
            return Mono.just(product);
        });
        when(inventorySyncClient.syncProduct(any(InventoryProductSyncRequest.class))).thenReturn(Mono.empty());

        StepVerifier.create(catalogService.createProduct(request))
                .expectNextMatches(product -> product.getUnitPrice().compareTo(new BigDecimal("450.00")) == 0
                        && product.getActive())
                .verifyComplete();

        verify(inventorySyncClient).syncProduct(any(InventoryProductSyncRequest.class));
        ArgumentCaptor<CatalogProduct> productCaptor = ArgumentCaptor.forClass(CatalogProduct.class);
        verify(productRepository).save(productCaptor.capture());
        org.junit.jupiter.api.Assertions.assertEquals("SKU-CHA-010", productCaptor.getValue().getSku());
    }
}
