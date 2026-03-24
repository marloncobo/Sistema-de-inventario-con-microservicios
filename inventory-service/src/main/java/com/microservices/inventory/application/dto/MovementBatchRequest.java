package com.microservices.inventory.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class MovementBatchRequest {
    @NotEmpty
    private List<@NotNull @Valid MovementRequest> movements;

    public List<MovementRequest> getMovements() {
        return movements;
    }

    public void setMovements(List<MovementRequest> movements) {
        this.movements = movements;
    }
}
