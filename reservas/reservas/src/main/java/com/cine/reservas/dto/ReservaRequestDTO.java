package com.cine.reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de entrada para crear una nueva reserva")
public class ReservaRequestDTO {

    @Schema(description = "ID del usuario que realiza la reserva", example = "1", required = true)
    @NotNull(message = "El usuario_id es obligatorio")
    private Long usuario_id;

    @Schema(description = "ID de la función (horario de la película)", example = "1", required = true)
    @NotNull(message = "El funcion_id es obligatorio")
    private Long funcion_id;

    @Schema(description = "Cantidad de asientos a reservar", example = "2", required = true)
    @NotNull(message = "La cantidad de asientos es obligatoria")
    @Positive(message = "La cantidad de asientos debe ser mayor a 0")
    private Integer cantidad_de_asientos;

    @Schema(description = "Monto total de la reserva", example = "15000", required = true)
    @NotNull(message = "El total es obligatorio")
    @Positive(message = "El total debe ser mayor a 0")
    private Double total;
}