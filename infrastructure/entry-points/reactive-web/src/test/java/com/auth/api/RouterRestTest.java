package com.auth.api;

import com.auth.api.dto.UserRequest;
import com.auth.api.errorHandler.GlobalErrorHandler;
import com.auth.api.utils.ValidationErrorResponse;
import com.auth.usecase.exception.ConflictException;
import com.auth.usecase.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(RouterRestTest.class)
@ContextConfiguration(classes = {RouterRest.class, RouterRestTest.class})
@Import(GlobalErrorHandler.class)
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private Handler handler;

    private UserRequest validRequest() {
        return new UserRequest(
                "Flor",
                "Baza",
                LocalDate.of(2001, 4, 20),
                "123456789",
                "3000000000",
                "flor@test.com",
                "Calle 123",
                BigDecimal.valueOf(1000000),
                1L
        );
    }

    @Test
    void createUser_shouldReturn200_whenValidRequest() {
        when(handler.createUser(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .bodyValue(validRequest())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void createUser_shouldReturn400_whenMissingName() {
        UserRequest invalid = new UserRequest(
                "",
                "Baza",
                LocalDate.of(1990, 1, 1),
                "123456789",
                "3000000000",
                "flor@test.com",
                "Calle 123",
                BigDecimal.valueOf(1000000),
                1L
        );

        when(handler.createUser(any()))
                .thenReturn(Mono.error(new ValidationErrorResponse(List.of("El nombre es obligatorio"))));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .bodyValue(invalid)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createUser_shouldReturn404_whenRoleNotFound() {
        when(handler.createUser(any()))
                .thenReturn(Mono.error(new ResourceNotFoundException("Rol no existe")));

        webTestClient.mutate()
                .build()
                .post()
                .uri("/api/v1/usuarios")
                .bodyValue(validRequest())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createUser_shouldReturn409_whenEmailConflict() {
        when(handler.createUser(any()))
                .thenReturn(Mono.error(new ConflictException("Correo ya registrado")));

        webTestClient.mutate()
                .build()
                .post()
                .uri("/api/v1/usuarios")
                .bodyValue(validRequest())
                .exchange()
                .expectStatus().isEqualTo(409);
    }
}
