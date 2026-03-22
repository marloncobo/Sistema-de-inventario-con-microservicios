package com.microservices.auth.application.service;

import com.microservices.auth.application.dto.AuthRequest;
import com.microservices.auth.application.dto.AuthResponse;
import com.microservices.auth.infrastructure.repository.UserRepository;
import com.microservices.auth.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public Mono<AuthResponse> login(AuthRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> BCrypt.checkpw(request.getPassword(), user.getPasswordHash()))
                .map(user -> new AuthResponse(jwtUtil.generateToken(user)))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas")));
    }
}
