package com.auth.api.errorHandler;

import com.auth.api.utils.ValidationErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-2)
public class GlobalErrorHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @NonNull
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        HttpStatus status;
        Object message;

        switch (ex.getClass().getSimpleName()) {
            case "ValidationErrorResponse" -> {
                status = HttpStatus.BAD_REQUEST;
                message = ((ValidationErrorResponse) ex).getErrores();
            }

            case "AccessDeniedException" -> {
                status = HttpStatus.FORBIDDEN;
                message = ex.getMessage();
            }
            case "ConflictException" -> {
                status = HttpStatus.CONFLICT;
                message = ex.getMessage();
            }
            case "ResourceNotFoundException" -> {
                status = HttpStatus.NOT_FOUND;
                message = ex.getMessage();
            }
            default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                message = "Error inesperado: " + ex.getMessage();
            }
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of("Error", message);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(body);
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(bytes)));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
