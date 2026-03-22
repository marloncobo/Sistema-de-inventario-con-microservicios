package com.microservices.auth.infrastructure.controller;

import com.microservices.auth.application.dto.AuthRequest;
import com.microservices.auth.application.dto.AuthResponse;
import com.microservices.auth.application.dto.UserRequest;
import com.microservices.auth.application.dto.UserResponse;
import com.microservices.auth.application.service.AuthService;
import com.microservices.auth.application.service.RegistrationService;
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
    private final RegistrationService registrationService;

    @PostMapping("/login")
    public Mono<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public Mono<UserResponse> register(@Valid @RequestBody UserRequest request) {
        return registrationService.register(request);
    }
}
