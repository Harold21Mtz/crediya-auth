package com.auth.api.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Component
public class ValidateDtos {

    private final SmartValidator validator;

    public <T> Mono<T> validate(ServerRequest request, Class<T> clazz) {
        return request.bodyToMono(clazz)
                .flatMap(body -> {
                            var errors = new BeanPropertyBindingResult(body, clazz.getName());
                            validator.validate(body, errors);

                            if (errors.hasErrors()) {
                                List<String> messages = errors.getAllErrors()
                                        .stream()
                                        .map(ObjectError::getDefaultMessage)
                                        .toList();

                                return Mono.error(new ValidationErrorResponse(messages));
                            }
                            return Mono.just(body);
                        }
                );
    }
}