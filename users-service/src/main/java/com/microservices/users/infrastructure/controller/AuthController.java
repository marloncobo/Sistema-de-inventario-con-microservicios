package com.microservices.users.infrastructure.controller;

import com.microservices.users.application.dto.AuthRequest;
import com.microservices.users.application.dto.AuthResponse;
import com.microservices.users.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Mono<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return authService.login(request);
    }
}
