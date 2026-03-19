package com.microservices.users.application.dto;

import com.microservices.users.domain.Role;
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
