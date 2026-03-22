package com.microservices.auth.application.dto;

import com.microservices.auth.domain.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String username;
    private Role role;
    private LocalDateTime createdAt;
}
