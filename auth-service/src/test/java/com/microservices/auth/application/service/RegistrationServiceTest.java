package com.microservices.auth.application.service;

import com.microservices.auth.application.dto.UserRequest;
import com.microservices.auth.domain.Role;
import com.microservices.auth.domain.User;
import com.microservices.auth.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void registerAlwaysCreatesUserRole() {
        UserRequest request = new UserRequest();
        request.setUsername("user1");
        request.setPassword("secret123");

        when(userRepository.findByUsername("user1")).thenReturn(Mono.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(registrationService.register(request))
                .expectNextMatches(response -> response.getRole() == Role.USER)
                .verifyComplete();
    }
}
