package com.cine.pagos.controller;

import com.cine.pagos.assembler.PagoModelAssembler;
import com.cine.pagos.dto.PagoRequestDTO;
import com.cine.pagos.dto.PagoResponseDTO;
import com.cine.pagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Endpoints para gestionar pagos de reservas")
public class PagoController {

    private final PagoService pagoService;
    private final PagoModelAssembler assembler;

    @PostMapping
    @Operation(summary = "Procesar un pago", description = "Procesa el pago de una reserva (simula aprobación, rechazo o timeout)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago procesado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<PagoResponseDTO>> procesarPago(@Valid @RequestBody PagoRequestDTO dto) {
        PagoResponseDTO pago = pagoService.procesarPago(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(pago));
    }

    @GetMapping("/reserva/{reservaId}")
    @Operation(summary = "Obtener pago por ID de reserva", description = "Busca el pago asociado a una reserva específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe pago para esta reserva")
    })
    public ResponseEntity<EntityModel<PagoResponseDTO>> obtenerPorReserva(@Parameter(description = "ID de la reserva", required = true) @PathVariable Long reservaId) {
        return pagoService.obtenerPagoPorReserva(reservaId)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID", description = "Busca un pago por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<EntityModel<PagoResponseDTO>> obtenerPorId(@Parameter(description = "ID del pago", required = true) @PathVariable Long id) {
        return pagoService.obtenerPagoPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}