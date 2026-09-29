package com.todocodeacademy.travelmate.dto;

import com.todocodeacademy.travelmate.model.enums.TipoDestino;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record DestinoRequest(
        @NotBlank String nombre,
        @NotBlank String pais,
        @NotBlank String ciudad,
        @NotNull TipoDestino tipo,
        @NotBlank @Size(max = 1000) String descripcion,
        @NotNull BigDecimal temperaturaPromedio,
        @NotNull Boolean aptoFamilias,
        @NotNull Boolean aptoParejas,
        @NotNull Boolean tranquilo,
        @NotBlank String mejorEpoca
) {
}
