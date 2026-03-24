package com.microservices.sales.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("sales_order_items")
public class SalesOrderItem implements Persistable<UUID> {
    @Id
    private UUID id;
    private UUID salesOrderId;
    private UUID productId;
    private Integer quantity;
    private BigDecimal price;

    public SalesOrderItem() {
    }

    public SalesOrderItem(UUID id, UUID salesOrderId, UUID productId, Integer quantity, BigDecimal price) {
        this.id = id;
        this.salesOrderId = salesOrderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(UUID salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
