package com.auth.api;

import com.auth.api.dto.UserRequest;
import com.auth.api.dto.ValidationErrorResponse;
import com.auth.api.errorHandler.GlobalErrorHandler;
import com.auth.api.mapper.UserMapper;
import com.auth.model.user.User;
import com.auth.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserMapper userMapper;
    private final SpringValidatorAdapter validator;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UserRequest.class)
                .flatMap(userRequest -> {
                    var errors = new BeanPropertyBindingResult(userRequest, UserRequest.class.getName());
                    validator.validate(userRequest, errors);

                    if (errors.hasErrors()) {
                        List<String> messages = errors.getAllErrors()
                                .stream()
                                .map(ObjectError::getDefaultMessage)
                                .toList();

                        return ServerResponse
                                .badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ValidationErrorResponse(messages));
                    }
                    return userUseCase.createUser(userMapper.toModel(userRequest))
                            .then(ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue("{\"message\": \"Usuario creado\"}"));
                }).onErrorResume(GlobalErrorHandler::handle);
    }

}

