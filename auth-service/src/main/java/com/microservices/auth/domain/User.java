package com.microservices.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User implements Persistable<UUID> {
    @Id
    private UUID id;
    private String username;
    private String passwordHash;
    private Role role;
    private LocalDateTime createdAt;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
