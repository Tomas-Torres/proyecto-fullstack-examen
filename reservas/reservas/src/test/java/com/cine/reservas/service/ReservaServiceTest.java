package com.cine.reservas.service;

import com.cine.reservas.client.PagoCliente;
import com.cine.reservas.client.PeliculaCliente;
import com.cine.reservas.client.UsuarioCliente;
import com.cine.reservas.dto.PagoResponseDTO;
import com.cine.reservas.dto.ReservaRequestDTO;
import com.cine.reservas.dto.ReservaResponseDTO;
import com.cine.reservas.model.Resrvas_model;
import com.cine.reservas.repository.Reserva_repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ReservaServiceTest {

    @Autowired
    private ReservaService reservaService;

    @MockitoBean
    private Reserva_repository reservaRepository;

    @MockitoBean
    private PagoCliente pagoCliente;

    @MockitoBean
    private UsuarioCliente usuarioCliente;

    @MockitoBean
    private PeliculaCliente peliculaCliente;

    private ReservaRequestDTO requestDTO;
    private Resrvas_model reservaEntity;

    @BeforeEach
    void setUp() {
        requestDTO = new ReservaRequestDTO();
        requestDTO.setUsuario_id(1L);
        requestDTO.setFuncion_id(1L);
        requestDTO.setCantidad_de_asientos(2);
        requestDTO.setTotal(15000.0);

        reservaEntity = new Resrvas_model();
        reservaEntity.setId(1L);
        reservaEntity.setUsuarioId(1L);
        reservaEntity.setFuncionId(1L);
        reservaEntity.setCantidadDeAsientos(2);
        reservaEntity.setTotal(15000.0);
        reservaEntity.setEstado("PENDIENTE");
        reservaEntity.setFechaReserva(LocalDateTime.now());
    }

    // ─── obtenerTodas() ──────────────────────────────────────────────

    @Test
    public void testObtenerTodas() {
        // Given
        when(reservaRepository.findAll()).thenReturn(List.of(reservaEntity));

        // When
        List<ReservaResponseDTO> resultado = reservaService.obtenerTodas();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(reservaEntity.getId(), resultado.get(0).getId());
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerTodas_CuandoNoHayReservas() {
        // Given
        when(reservaRepository.findAll()).thenReturn(List.of());

        // When
        List<ReservaResponseDTO> resultado = reservaService.obtenerTodas();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(reservaRepository, times(1)).findAll();
    }

    // ─── obtenerPorId() ──────────────────────────────────────────────

    @Test
    public void testObtenerPorId_CuandoExiste() {
        // Given
        Long id = 1L;
        when(reservaRepository.findById(id)).thenReturn(Optional.of(reservaEntity));

        // When
        Optional<ReservaResponseDTO> resultado = reservaService.obtenerPorId(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(reservaEntity.getId(), resultado.get().getId());
        verify(reservaRepository, times(1)).findById(id);
    }

    @Test
    public void testObtenerPorId_CuandoNoExiste() {
        // Given
        Long id = 999L;
        when(reservaRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<ReservaResponseDTO> resultado = reservaService.obtenerPorId(id);

        // Then
        assertTrue(resultado.isEmpty());
        verify(reservaRepository, times(1)).findById(id);
    }

    // ─── obtenerPorUsuario() ─────────────────────────────────────────

    @Test
    public void testObtenerPorUsuario() {
        // Given
        Long usuarioId = 1L;
        when(reservaRepository.findByUsuarioId(usuarioId)).thenReturn(List.of(reservaEntity));

        // When
        List<ReservaResponseDTO> resultado = reservaService.obtenerPorUsuario(usuarioId);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(reservaRepository, times(1)).findByUsuarioId(usuarioId);
    }

    @Test
    public void testObtenerPorUsuario_CuandoNoHayReservas() {
        // Given
        Long usuarioId = 999L;
        when(reservaRepository.findByUsuarioId(usuarioId)).thenReturn(List.of());

        // When
        List<ReservaResponseDTO> resultado = reservaService.obtenerPorUsuario(usuarioId);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(reservaRepository, times(1)).findByUsuarioId(usuarioId);
    }

    // ─── obtenerPorFuncion() ─────────────────────────────────────────

    @Test
    public void testObtenerPorFuncion() {
        // Given
        Long funcionId = 1L;
        when(reservaRepository.findByFuncionId(funcionId)).thenReturn(List.of(reservaEntity));

        // When
        List<ReservaResponseDTO> resultado = reservaService.obtenerPorFuncion(funcionId);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(reservaRepository, times(1)).findByFuncionId(funcionId);
    }

    @Test
    public void testObtenerPorFuncion_CuandoNoHayReservas() {
        // Given
        Long funcionId = 999L;
        when(reservaRepository.findByFuncionId(funcionId)).thenReturn(List.of());

        // When
        List<ReservaResponseDTO> resultado = reservaService.obtenerPorFuncion(funcionId);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(reservaRepository, times(1)).findByFuncionId(funcionId);
    }

    // ─── obtenerPorEstado() ──────────────────────────────────────────

    @Test
    public void testObtenerPorEstado() {
        // Given
        String estado = "CONFIRMADA";
        reservaEntity.setEstado(estado);
        when(reservaRepository.findByEstado(estado)).thenReturn(List.of(reservaEntity));

        // When
        List<ReservaResponseDTO> resultado = reservaService.obtenerPorEstado(estado);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(estado, resultado.get(0).getEstado());
        verify(reservaRepository, times(1)).findByEstado(estado);
    }

    @Test
    public void testObtenerPorEstado_CuandoNoHayReservas() {
        // Given
        String estado = "CANCELADA";
        when(reservaRepository.findByEstado(estado)).thenReturn(List.of());

        // When
        List<ReservaResponseDTO> resultado = reservaService.obtenerPorEstado(estado);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(reservaRepository, times(1)).findByEstado(estado);
    }

    // ─── guardar() ────────────────────────────────────────────────────

    @Test
    public void testGuardar_ConPagoAprobado() {
        // Given
        Map<String, Boolean> usuarioRespuesta = Map.of("existe", true);
        when(usuarioCliente.validarUsuario(any(Long.class))).thenReturn(usuarioRespuesta);

        PagoResponseDTO pagoResponse = new PagoResponseDTO();
        pagoResponse.setEstado("APROBADO");
        when(pagoCliente.procesarPago(any())).thenReturn(pagoResponse);

        when(reservaRepository.save(any(Resrvas_model.class))).thenReturn(reservaEntity);

        // When
        ReservaResponseDTO resultado = reservaService.guardar(requestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("CONFIRMADA", resultado.getEstado());
        verify(usuarioCliente, times(1)).validarUsuario(any(Long.class));
        verify(pagoCliente, times(1)).procesarPago(any());
        verify(reservaRepository, times(2)).save(any(Resrvas_model.class));
    }

    @Test
    public void testGuardar_ConPagoRechazado() {
        // Given
        Map<String, Boolean> usuarioRespuesta = Map.of("existe", true);
        when(usuarioCliente.validarUsuario(any(Long.class))).thenReturn(usuarioRespuesta);

        PagoResponseDTO pagoResponse = new PagoResponseDTO();
        pagoResponse.setEstado("RECHAZADO");
        when(pagoCliente.procesarPago(any())).thenReturn(pagoResponse);

        when(reservaRepository.save(any(Resrvas_model.class))).thenReturn(reservaEntity);

        // When
        ReservaResponseDTO resultado = reservaService.guardar(requestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("CANCELADA", resultado.getEstado());
        verify(usuarioCliente, times(1)).validarUsuario(any(Long.class));
        verify(pagoCliente, times(1)).procesarPago(any());
        verify(reservaRepository, times(2)).save(any(Resrvas_model.class));
    }

    @Test
    public void testGuardar_UsuarioNoExiste_LanzaExcepcion() {
        // Given
        Map<String, Boolean> usuarioRespuesta = Map.of("existe", false);
        when(usuarioCliente.validarUsuario(any(Long.class))).thenReturn(usuarioRespuesta);

        // When & Then
        assertThrows(RuntimeException.class, () -> reservaService.guardar(requestDTO));
        verify(usuarioCliente, times(1)).validarUsuario(any(Long.class));
        verify(pagoCliente, never()).procesarPago(any());
        verify(reservaRepository, never()).save(any(Resrvas_model.class));
    }

    @Test
    public void testGuardar_PagoTimeout_ReservaCancelada() {
        // Given
        Map<String, Boolean> usuarioRespuesta = Map.of("existe", true);
        when(usuarioCliente.validarUsuario(any(Long.class))).thenReturn(usuarioRespuesta);

        PagoResponseDTO pagoResponse = new PagoResponseDTO();
        pagoResponse.setEstado("TIMEOUT");
        when(pagoCliente.procesarPago(any())).thenReturn(pagoResponse);

        when(reservaRepository.save(any(Resrvas_model.class))).thenReturn(reservaEntity);

        // When
        ReservaResponseDTO resultado = reservaService.guardar(requestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("CANCELADA", resultado.getEstado());
        verify(reservaRepository, times(2)).save(any(Resrvas_model.class));
    }

    // ─── cancelar() ──────────────────────────────────────────────────

    @Test
    public void testCancelar_CuandoExiste() {
        // Given
        Long id = 1L;
        when(reservaRepository.findById(id)).thenReturn(Optional.of(reservaEntity));
        when(reservaRepository.save(any(Resrvas_model.class))).thenReturn(reservaEntity);

        // When
        Optional<ReservaResponseDTO> resultado = reservaService.cancelar(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("CANCELADA", resultado.get().getEstado());
        verify(reservaRepository, times(1)).findById(id);
        verify(reservaRepository, times(1)).save(any(Resrvas_model.class));
    }

    @Test
    public void testCancelar_CuandoNoExiste() {
        // Given
        Long id = 999L;
        when(reservaRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<ReservaResponseDTO> resultado = reservaService.cancelar(id);

        // Then
        assertTrue(resultado.isEmpty());
        verify(reservaRepository, times(1)).findById(id);
        verify(reservaRepository, never()).save(any(Resrvas_model.class));
    }

    // ─── confirmar() ──────────────────────────────────────────────────

    @Test
    public void testConfirmar_CuandoExiste() {
        // Given
        Long id = 1L;
        when(reservaRepository.findById(id)).thenReturn(Optional.of(reservaEntity));
        when(reservaRepository.save(any(Resrvas_model.class))).thenReturn(reservaEntity);

        // When
        Optional<ReservaResponseDTO> resultado = reservaService.confirmar(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("CONFIRMADA", resultado.get().getEstado());
        verify(reservaRepository, times(1)).findById(id);
        verify(reservaRepository, times(1)).save(any(Resrvas_model.class));
    }

    @Test
    public void testConfirmar_CuandoNoExiste() {
        // Given
        Long id = 999L;
        when(reservaRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<ReservaResponseDTO> resultado = reservaService.confirmar(id);

        // Then
        assertTrue(resultado.isEmpty());
        verify(reservaRepository, times(1)).findById(id);
        verify(reservaRepository, never()).save(any(Resrvas_model.class));
    }

    // ─── eliminar() ──────────────────────────────────────────────────

    @Test
    public void testEliminar() {
        // Given
        Long id = 1L;
        doNothing().when(reservaRepository).deleteById(id);

        // When
        reservaService.eliminar(id);

        // Then
        verify(reservaRepository, times(1)).deleteById(id);
    }

    // ─── Métodos para tests ──────────────────────────────────────────

    @Test
    public void testObtenerTodasEntity() {
        // Given
        when(reservaRepository.findAll()).thenReturn(List.of(reservaEntity));

        // When
        List<Resrvas_model> resultado = reservaService.obtenerTodasEntity();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerPorIdEntity_CuandoExiste() {
        // Given
        Long id = 1L;
        when(reservaRepository.findById(id)).thenReturn(Optional.of(reservaEntity));

        // When
        Optional<Resrvas_model> resultado = reservaService.obtenerPorIdEntity(id);

        // Then
        assertTrue(resultado.isPresent());
        verify(reservaRepository, times(1)).findById(id);
    }

    @Test
    public void testObtenerPorIdEntity_CuandoNoExiste() {
        // Given
        Long id = 999L;
        when(reservaRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Resrvas_model> resultado = reservaService.obtenerPorIdEntity(id);

        // Then
        assertTrue(resultado.isEmpty());
        verify(reservaRepository, times(1)).findById(id);
    }
}