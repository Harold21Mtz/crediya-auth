package com.auth.api.errorHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class GlobalErrorHandler {

    public static Mono<ServerResponse> handle(Throwable ex) {
        HttpStatus status;
        String error = switch (ex.getClass().getSimpleName()) {
            case "AccessDeniedException" -> {
                status = HttpStatus.FORBIDDEN;
                yield ex.getMessage();
            }
            case "ConflictException" -> {
                status = HttpStatus.CONFLICT;
                yield ex.getMessage();
            }
            case "ResourceNotFoundException" -> {
                status = HttpStatus.NOT_FOUND;
                yield ex.getMessage();
            }
            default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                yield "Error inesperado: " + ex.getMessage();
            }
        };

        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"messageError\": \"" + error + "\"}");
    }
}
