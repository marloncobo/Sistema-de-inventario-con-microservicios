package com.microservices.inventory.application.dto;

import com.microservices.inventory.domain.MovementType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class MovementRequest {
    @NotNull
    private UUID productId;
    @NotNull
    private MovementType type;
    @NotNull
    @Positive
    private Integer quantity;
}
