package com.auth.api.dto;

import java.util.List;

public record ValidationErrorResponse(List<String> errores) {
}
