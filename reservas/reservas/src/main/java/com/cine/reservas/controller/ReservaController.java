package com.cine.reservas.controller;

import com.cine.reservas.dto.ReservaRequestDTO;
import com.cine.reservas.dto.ReservaResponseDTO;
import com.cine.reservas.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Endpoints para gestionar reservas de cine")
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping
    @Operation(summary = "Obtener todas las reservas", description = "Lista todas las reservas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID", description = "Busca una reserva por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaResponseDTO> obtenerPorId(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Long id) {
        return reservaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Historial de reservas por usuario", description = "Lista todas las reservas de un usuario específico")
    @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerPorUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(reservaService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/funcion/{funcionId}")
    @Operation(summary = "Reservas por función", description = "Lista todas las reservas de una función específica")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerPorFuncion(
            @Parameter(description = "ID de la función", required = true)
            @PathVariable Long funcionId) {
        return ResponseEntity.ok(reservaService.obtenerPorFuncion(funcionId));
    }

    @GetMapping("/pelicula/{peliculaId}")
    @Operation(summary = "Reservas por película", description = "Lista todas las reservas asociadas a una película")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerPorPelicula(
            @Parameter(description = "ID de la película", required = true)
            @PathVariable Long peliculaId) {
        return ResponseEntity.ok(reservaService.obtenerPorFuncion(peliculaId));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Reservas por estado", description = "Lista todas las reservas con un estado específico (PENDIENTE, CONFIRMADA, CANCELADA)")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerPorEstado(
            @Parameter(description = "Estado de la reserva", required = true, example = "PENDIENTE")
            @PathVariable String estado) {
        return ResponseEntity.ok(reservaService.obtenerPorEstado(estado));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reserva", description = "Crea una reserva y procesa el pago automáticamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario no existe")
    })
    public ResponseEntity<ReservaResponseDTO> crear(@Valid @RequestBody ReservaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservaService.guardar(dto));
    }

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una reserva", description = "Cambia el estado de una reserva a CANCELADA")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaResponseDTO> cancelar(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Long id) {
        return reservaService.cancelar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar una reserva", description = "Cambia el estado de una reserva a CONFIRMADA")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva confirmada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaResponseDTO> confirmar(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Long id) {
        return reservaService.confirmar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reserva", description = "Elimina permanentemente una reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Long id) {
        if (reservaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}