package com.auth.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotBlank(message = "El apellido es obligatorio")
        String lastname,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        LocalDate birthDate,

        @NotBlank(message = "El documento es obligatorio")
        String documentNumber,

        @NotBlank(message = "El teléfono es obligatorio")
        String phone,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        String email,

        @NotBlank(message = "La dirección es obligatoria")
        String address,

        @NotNull(message = "El salario base es obligatorio")
        @DecimalMin(value = "0.0", message = "El salario base debe ser mayor o igual a 0")
        @DecimalMax(value = "15000000.0", message = "El salario base no puede superar los 15 millones")
        BigDecimal baseSalary,

        @NotNull(message = "El rol es obligatorio")
        Long roleId) {
}
