package com.cine.reservas.service;

import com.cine.reservas.client.PagoCliente;
import com.cine.reservas.client.PeliculaCliente;
import com.cine.reservas.client.UsuarioCliente;
import com.cine.reservas.dto.PagoRequestDTO;
import com.cine.reservas.dto.PagoResponseDTO;
import com.cine.reservas.dto.PeliculaExisteDTO;
import com.cine.reservas.dto.ReservaRequestDTO;
import com.cine.reservas.dto.ReservaResponseDTO;
import com.cine.reservas.model.Resrvas_model;
import com.cine.reservas.repository.Reserva_repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {

    private final Reserva_repository reservaRepository;
    private final PagoCliente pagoCliente;           // Cliente para ms-pagos
    private final UsuarioCliente usuarioCliente;     // Cliente para ms-usuarios
    private final PeliculaCliente peliculaCliente;   // Cliente para ms-peliculas

    // Mapeo de Entidad → DTO Response
    private ReservaResponseDTO mapToDTO(Resrvas_model reserva) {
        return new ReservaResponseDTO(
                reserva.getId(),
                reserva.getUsuarioId(),
                reserva.getFuncionId(),
                reserva.getCantidadDeAsientos(),
                reserva.getTotal(),
                reserva.getEstado(),
                reserva.getFechaReserva()
        );
    }

    // Mapeo de DTO Request → Entidad
    private Resrvas_model mapToEntity(ReservaRequestDTO dto) {
        Resrvas_model reserva = new Resrvas_model();
        reserva.setUsuarioId(dto.getUsuario_id());
        reserva.setFuncionId(dto.getFuncion_id());
        reserva.setCantidadDeAsientos(dto.getCantidad_de_asientos());
        reserva.setTotal(dto.getTotal());
        reserva.setEstado("PENDIENTE");
        reserva.setFechaReserva(LocalDateTime.now());
        return reserva;
    }

    // Obtener todas las reservas
    public List<ReservaResponseDTO> obtenerTodas() {
        return reservaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Obtener reserva por ID
    public Optional<ReservaResponseDTO> obtenerPorId(Long id) {
        return reservaRepository.findById(id).map(this::mapToDTO);
    }

    // Obtener reservas por usuario
    public List<ReservaResponseDTO> obtenerPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Obtener reservas por función
    public List<ReservaResponseDTO> obtenerPorFuncion(Long funcionId) {
        return reservaRepository.findByFuncionId(funcionId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Obtener reservas por estado
    public List<ReservaResponseDTO> obtenerPorEstado(String estado) {
        return reservaRepository.findByEstado(estado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ──────────────────────────────────────────────────────
    // MÉTODO PRINCIPAL: Crear reserva + validaciones + pago
    // ──────────────────────────────────────────────────────
    public ReservaResponseDTO guardar(ReservaRequestDTO dto) {
        // 1. Validar que el usuario existe (ms-usuarios)
        log.info("Validando usuario ID: {}", dto.getUsuario_id());
        Map<String, Boolean> respuesta = usuarioCliente.validarUsuario(dto.getUsuario_id());
        Boolean existe = respuesta.get("existe");
        if (existe == null || !existe) {
            throw new RuntimeException("Usuario no válido o inactivo con ID: " + dto.getUsuario_id());
        }
        log.info("Usuario validado correctamente: ID {}", dto.getUsuario_id());

        // 2. Validar que la película existe (ms-peliculas)
        // Nota: Como la reserva usa funcionId, necesitas mapear función → película.
        // Por ahora, asumimos que el funcionId es válido o recibimos el peliculaId en el DTO.
        // Si tu DTO tiene pelicula_id, descomenta la siguiente validación:
        /*
        log.info("Validando película ID: {}", dto.getPelicula_id());
        PeliculaExisteDTO pelicula = peliculaCliente.validarPelicula(dto.getPelicula_id());
        if (!pelicula.isExiste()) {
            throw new RuntimeException("Película no existe con ID: " + dto.getPelicula_id());
        }
        log.info("Película validada correctamente: ID {}", dto.getPelicula_id());
        */

        // 3. Guardar reserva como PENDIENTE
        Resrvas_model reserva = mapToEntity(dto);
        Resrvas_model reservaGuardada = reservaRepository.save(reserva);
        log.info("Reserva creada con ID: {} en estado PENDIENTE", reservaGuardada.getId());

        // 4. Llamar a ms-pagos para procesar el pago
        try {
            PagoRequestDTO pagoRequest = new PagoRequestDTO(
                    reservaGuardada.getId(),
                    dto.getTotal(),
                    "TARJETA"
            );

            PagoResponseDTO pagoResponse = pagoCliente.procesarPago(pagoRequest);
            log.info("Respuesta de ms-pagos: {}", pagoResponse.getEstado());

            // 5. Actualizar estado según respuesta del pago
            if ("APROBADO".equals(pagoResponse.getEstado())) {
                reservaGuardada.setEstado("CONFIRMADA");
                log.info("Pago aprobado, reserva {} confirmada", reservaGuardada.getId());
            } else {
                reservaGuardada.setEstado("CANCELADA");
                log.warn("Pago {}: reserva {} cancelada", pagoResponse.getEstado(), reservaGuardada.getId());
            }

            return mapToDTO(reservaRepository.save(reservaGuardada));

        } catch (Exception e) {
            // Si hay error al llamar a ms-pagos, cancelar la reserva
            reservaGuardada.setEstado("CANCELADA");
            reservaRepository.save(reservaGuardada);
            log.error("Error al llamar a ms-pagos: {}", e.getMessage());
            throw new RuntimeException("Error al procesar el pago: " + e.getMessage());
        }
    }

    // Cancelar una reserva
    public Optional<ReservaResponseDTO> cancelar(Long id) {
        return reservaRepository.findById(id).map(reserva -> {
            reserva.setEstado("CANCELADA");
            log.info("Reserva {} cancelada manualmente", id);
            return mapToDTO(reservaRepository.save(reserva));
        });
    }

    // Confirmar una reserva manualmente
    public Optional<ReservaResponseDTO> confirmar(Long id) {
        return reservaRepository.findById(id).map(reserva -> {
            reserva.setEstado("CONFIRMADA");
            log.info("Reserva {} confirmada manualmente", id);
            return mapToDTO(reservaRepository.save(reserva));
        });
    }

    // Eliminar una reserva
    public void eliminar(Long id) {
        reservaRepository.deleteById(id);
        log.info("Reserva {} eliminada permanentemente", id);
    }

    // ============================================================
    // MÉTODOS PARA TESTS (no usar en producción)
    // ============================================================

    // Método para test (devuelve lista de entidades, no DTOs)
    public List<Resrvas_model> obtenerTodasEntity() {
        return reservaRepository.findAll();
    }

    // Método para test (devuelve Optional de entidad, no DTO)
    public Optional<Resrvas_model> obtenerPorIdEntity(Long id) {
        return reservaRepository.findById(id);
    }
}