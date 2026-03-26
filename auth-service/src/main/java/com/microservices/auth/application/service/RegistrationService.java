package com.microservices.auth.application.service;

import com.microservices.auth.application.dto.UserRequest;
import com.microservices.auth.application.dto.UserResponse;
import com.microservices.auth.domain.Role;
import com.microservices.auth.domain.User;
import com.microservices.auth.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;

    public Mono<UserResponse> register(UserRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.CONFLICT, "Username already exists"));
                    }

                    String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

                    User user = User.builder()
                            .username(request.getUsername())
                            .passwordHash(hashedPassword)
                            .role(Role.USER)
                            .createdAt(LocalDateTime.now())
                            .build();

                    return userRepository.save(user).map(this::mapToResponse);
                });
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
