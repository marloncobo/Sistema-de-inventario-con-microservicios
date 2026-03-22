package com.microservices.sales.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("sales_orders")
public class SalesOrder implements Persistable<UUID> {
    @Id
    private UUID id;
    private String reference;
    private String salesChannel;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
