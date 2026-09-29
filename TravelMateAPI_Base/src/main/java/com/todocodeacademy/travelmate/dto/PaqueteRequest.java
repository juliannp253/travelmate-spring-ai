package com.todocodeacademy.travelmate.dto;

import com.todocodeacademy.travelmate.model.enums.TipoTransporte;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record PaqueteRequest(
        @NotBlank String nombre,
        @NotNull @Min(1) Integer cantidadDias,
        @NotNull @DecimalMin("0.0") BigDecimal precioPorPersona,
        @NotNull Boolean incluyeTraslado,
        @NotNull Boolean incluyeDesayuno,
        @NotNull Boolean incluyeExcursiones,
        @NotNull TipoTransporte tipoTransporte,
        @NotBlank @Size(max = 1000) String descripcion,
        @NotNull Long destinoId,
        @NotNull Long hotelId,
        List<Long> actividadIds
) {
}
