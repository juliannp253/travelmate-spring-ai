package com.todocodeacademy.travelmate.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record HotelRequest(
        @NotBlank String nombre,
        @NotNull @Min(1) @Max(5) Integer estrellas,
        @NotNull @DecimalMin("0.0") BigDecimal precioNoche,
        @NotNull Boolean desayunoIncluido,
        @NotNull Boolean pileta,
        @NotNull Boolean spa,
        @NotNull Boolean aptoNinos,
        @NotNull Long destinoId
) {
}
