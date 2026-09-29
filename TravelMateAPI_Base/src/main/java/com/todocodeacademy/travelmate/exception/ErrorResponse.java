package com.todocodeacademy.travelmate.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime fecha,
        int estado,
        String error,
        String mensaje,
        Map<String, String> validaciones
) {
}
