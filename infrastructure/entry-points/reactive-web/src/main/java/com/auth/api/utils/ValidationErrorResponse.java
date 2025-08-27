package com.auth.api.utils;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationErrorResponse extends RuntimeException {
    private final List<String> errores;

    public ValidationErrorResponse(List<String> errores) {
        super("Error en validación de datos");
        this.errores = errores;
    }

}
