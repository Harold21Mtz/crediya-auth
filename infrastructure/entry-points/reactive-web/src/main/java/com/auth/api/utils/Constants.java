package com.auth.api.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String NAME_REQUIRED = "El nombre es obligatorio";
    public static final String LASTNAME_REQUIRED = "El apellido es obligatorio";
    public static final String BIRTHDATE_REQUIRED = "La fecha de nacimiento es obligatoria";
    public static final String DOCUMENT_REQUIRED = "El documento es obligatorio";
    public static final String DOCUMENT_CHARACTERS = "El documento debe tener entre 5 y 20 caracteres";
    public static final String PHONE_REQUIRED = "El teléfono es obligatorio";
    public static final String PHONE_CHARACTERS = "El teléfono debe tener máximo 12 caracteres";
    public static final String EMAIL_REQUIRED = "El correo es obligatorio";
    public static final String EMAIL_INVALID = "El correo no tiene un formato válido";
    public static final String ADDRESS_REQUIRED = "La dirección es obligatoria";
    public static final String BASE_SALARY_REQUIRED = "El salario base es obligatorio";
    public static final String BASE_SALARY_MIN = "El salario base debe ser mayor o igual a 0";
    public static final String BASE_SALARY_MAX = "El salario base no puede superar los 15 millones";
    public static final String ROLE_REQUIRED = "El rol es obligatorio";

}
