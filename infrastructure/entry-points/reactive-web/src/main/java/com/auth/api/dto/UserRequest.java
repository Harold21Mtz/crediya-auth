package com.auth.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.auth.api.utils.Constants.*;

public record UserRequest(

        @NotBlank(message = NAME_REQUIRED)
        String name,

        @NotBlank(message = LASTNAME_REQUIRED)
        String lastname,

        @NotNull(message = BIRTHDATE_REQUIRED)
        LocalDate birthDate,

        @NotBlank(message = DOCUMENT_REQUIRED)
        String documentNumber,

        @NotBlank(message = PHONE_REQUIRED)
        String phone,

        @NotBlank(message = EMAIL_REQUIRED)
        @Email(message = EMAIL_INVALID)
        String email,

        @NotBlank(message = ADDRESS_REQUIRED)
        String address,

        @NotNull(message = BASE_SALARY_REQUIRED)
        @DecimalMin(value = "0.0", message = BASE_SALARY_MIN)
        @DecimalMax(value = "15000000.0", message = BASE_SALARY_MAX)
        BigDecimal baseSalary,

        @NotNull(message = ROLE_REQUIRED)
        Long roleId) {
}
