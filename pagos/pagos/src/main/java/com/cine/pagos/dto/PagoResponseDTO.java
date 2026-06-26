package com.cine.pagos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con los datos de un pago")
public class PagoResponseDTO {

    @Schema(description = "ID único del pago", example = "1")
    private Long id;

    @Schema(description = "ID de la reserva asociada", example = "1")
    private Long reservaId;

    @Schema(description = "Monto pagado", example = "15000")
    private Double monto;

    @Schema(description = "Método de pago utilizado", example = "TARJETA")
    private String metodo;

    @Schema(description = "Estado del pago", example = "APROBADO", allowableValues = {"PROCESANDO", "APROBADO", "RECHAZADO", "TIMEOUT"})
    private String estado;

    @Schema(description = "Código único de transacción", example = "TXN-1741234567890-abc12345")
    private String codigoTransaccion;

    @Schema(description = "Fecha y hora del pago", example = "2026-06-12T10:30:00")
    private LocalDateTime fechaPago;

    @Schema(description = "Mensaje de error (solo si el pago fue rechazado o timeout)", example = "null")
    private String mensajeError;
}