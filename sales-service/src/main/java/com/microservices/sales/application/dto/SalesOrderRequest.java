package com.microservices.sales.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class SalesOrderRequest {
    @NotBlank
    private String salesChannel;
    @NotEmpty
    private List<@NotNull @Valid SalesItemRequest> items;

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public List<SalesItemRequest> getItems() {
        return items;
    }

    public void setItems(List<SalesItemRequest> items) {
        this.items = items;
    }
}
