package com.auth.api;

import com.auth.api.dto.UserRequest;
import com.auth.api.mapper.UserMapper;
import com.auth.api.utils.ValidateDtos;
import com.auth.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserMapper userMapper;
    private final ValidateDtos validateDtos;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return validateDtos.validate(request, UserRequest.class)
                .flatMap(userRequest -> userUseCase.createUser(userMapper.toModel(userRequest))
                        .then(ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("{\"message\": \"Usuario creado\"}")));
    }

    public Mono<ServerResponse> getUserByDocumentNumber(ServerRequest request) {
        return userUseCase.getUserByDocumentNumber(request.pathVariable("document_number"))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("email", user.getEmail())));
    }

}

