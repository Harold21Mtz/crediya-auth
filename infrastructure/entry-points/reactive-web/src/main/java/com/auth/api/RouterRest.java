package com.auth.api;

import com.auth.api.dto.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = { "application/json" },
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "createUser",
                    operation = @Operation(
                            operationId = "CrearUsuario",
                            summary = "Crear un nuevo usuario",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    description = "Crea un nuevo usuario en el sistema",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario creado"),
                                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                                    @ApiResponse(responseCode = "404", description = "El rol no existe"),
                                    @ApiResponse(responseCode = "409", description = "Correo ya registrado")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuario/{document_number}",
                    produces = { "application/json" },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "getUserByDocumentNumber",
                    operation = @Operation(
                            operationId = "ObtenerUsuarioPorDocumento",
                            summary = "Obtiene el correo de un usuario a partir de su número de documento",
                            parameters = {
                                    @Parameter(
                                            name = "document_number",
                                            description = "Número de documento del usuario",
                                            required = true,
                                            in = ParameterIn.PATH,
                                            schema = @Schema(type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UserRequest.class)
                                            )),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            )

    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::createUser)
                .andRoute(GET("/api/v1/usuario/{document_number}"), handler::getUserByDocumentNumber);
    }
}
