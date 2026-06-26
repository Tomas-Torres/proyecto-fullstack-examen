package com.cine.reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con los datos de una reserva")
public class ReservaResponseDTO {

    @Schema(description = "ID único de la reserva", example = "1")
    private Long id;

    @Schema(description = "ID del usuario que hizo la reserva", example = "1")
    private Long usuario_id;

    @Schema(description = "ID de la función asociada", example = "1")
    private Long funcion_id;

    @Schema(description = "Cantidad de asientos reservados", example = "2")
    private Integer cantidad_de_asientos;

    @Schema(description = "Monto total pagado", example = "15000")
    private Double total;

    @Schema(description = "Estado actual de la reserva", example = "CONFIRMADA", allowableValues = {"PENDIENTE", "CONFIRMADA", "CANCELADA"})
    private String estado;

    @Schema(description = "Fecha y hora en que se creó la reserva", example = "2026-06-12T10:30:00")
    private LocalDateTime fechaReserva;
}