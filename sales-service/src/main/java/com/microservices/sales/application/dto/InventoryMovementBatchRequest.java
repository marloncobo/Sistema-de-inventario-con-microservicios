package com.microservices.sales.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class InventoryMovementBatchRequest {
    @NotEmpty
    private List<@NotNull InventoryMovementRequest> movements;

    public InventoryMovementBatchRequest() {
    }

    public InventoryMovementBatchRequest(List<InventoryMovementRequest> movements) {
        this.movements = movements;
    }

    public List<InventoryMovementRequest> getMovements() {
        return movements;
    }

    public void setMovements(List<InventoryMovementRequest> movements) {
        this.movements = movements;
    }
}
