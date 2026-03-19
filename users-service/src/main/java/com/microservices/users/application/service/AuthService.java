package com.microservices.users.application.service;

import com.microservices.users.application.dto.AuthRequest;
import com.microservices.users.application.dto.AuthResponse;
import com.microservices.users.infrastructure.repository.UserRepository;
import com.microservices.users.infrastructure.security.JwtUtil;
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
