package com.cine.pagos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de entrada para procesar un pago")
public class PagoRequestDTO {

    @Schema(description = "ID de la reserva asociada", example = "1", required = true)
    @NotNull(message = "El ID de reserva es obligatorio")
    private Long reservaId;

    @Schema(description = "Monto a pagar", example = "15000", required = true)
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private Double monto;

    @Schema(description = "Método de pago", example = "TARJETA", required = true, allowableValues = {"TARJETA", "EFECTIVO", "TRANSFERENCIA"})
    @NotBlank(message = "El método de pago es obligatorio")
    private String metodo;
}