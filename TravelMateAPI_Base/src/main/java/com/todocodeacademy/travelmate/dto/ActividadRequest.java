package com.todocodeacademy.travelmate.dto;

import com.todocodeacademy.travelmate.model.enums.TipoActividad;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ActividadRequest(
        @NotBlank String nombre,
        @NotNull TipoActividad tipo,
        @NotBlank @Size(max = 800) String descripcion,
        @NotNull @Min(1) Integer duracionHoras,
        @NotNull @DecimalMin("0.0") BigDecimal precio,
        @NotNull Boolean aptaNinos,
        @NotNull Long destinoId
) {
}
