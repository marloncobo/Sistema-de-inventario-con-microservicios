package com.microservices.users.application.service;

import com.microservices.users.application.dto.AuthRequest;
import com.microservices.users.application.dto.AuthResponse;
import com.microservices.users.domain.Role;
import com.microservices.users.domain.User;
import com.microservices.users.infrastructure.repository.UserRepository;
import com.microservices.users.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginReturnsTokenWhenCredentialsAreValid() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("admin")
                .passwordHash("$2a$10$W2iXQJ.H/A438mIf0B5i7OfJm.T6zH.5iT.lD3lJ.GZz.kU.e1jFq")
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .build();

        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        when(userRepository.findByUsername("admin")).thenReturn(Mono.just(user));
        when(jwtUtil.generateToken(any(User.class))).thenReturn("jwt-token");

        Mono<AuthResponse> result = authService.login(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> "jwt-token".equals(response.getToken()))
                .verifyComplete();
    }
}
