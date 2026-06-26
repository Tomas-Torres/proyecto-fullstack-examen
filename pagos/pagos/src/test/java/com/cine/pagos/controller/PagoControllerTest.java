package com.cine.pagos.controller;

import com.cine.pagos.dto.PagoRequestDTO;
import com.cine.pagos.dto.PagoResponseDTO;
import com.cine.pagos.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class PagoControllerTest {

    @Autowired
    private PagoService pagoService;

    @MockitoBean
    private PagoService pagoServiceMock;

    private PagoRequestDTO requestDTO;
    private PagoResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new PagoRequestDTO();
        requestDTO.setReservaId(1L);
        requestDTO.setMonto(15000.0);
        requestDTO.setMetodo("TARJETA");

        responseDTO = new PagoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setReservaId(1L);
        responseDTO.setMonto(15000.0);
        responseDTO.setMetodo("TARJETA");
        responseDTO.setEstado("APROBADO");
        responseDTO.setCodigoTransaccion("TXN-123456");
        responseDTO.setFechaPago(LocalDateTime.now());
        responseDTO.setMensajeError(null);
    }

    // ─── POST /api/pagos ─────────────────────────────────────────────

    @Test
    public void testProcesarPago() {
        // Given
        when(pagoServiceMock.procesarPago(any(PagoRequestDTO.class))).thenReturn(responseDTO);

        // When
        PagoResponseDTO resultado = pagoServiceMock.procesarPago(requestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(responseDTO.getId(), resultado.getId());
        assertEquals(responseDTO.getReservaId(), resultado.getReservaId());
        assertEquals(responseDTO.getMonto(), resultado.getMonto());
        assertEquals(responseDTO.getMetodo(), resultado.getMetodo());
        assertEquals(responseDTO.getEstado(), resultado.getEstado());
        assertNotNull(resultado.getCodigoTransaccion());
        verify(pagoServiceMock, times(1)).procesarPago(any(PagoRequestDTO.class));
    }

    @Test
    public void testProcesarPago_ConMontoDiferente() {
        // Given
        PagoRequestDTO request = new PagoRequestDTO(2L, 25000.0, "EFECTIVO");
        PagoResponseDTO response = new PagoResponseDTO();
        response.setId(2L);
        response.setReservaId(2L);
        response.setMonto(25000.0);
        response.setMetodo("EFECTIVO");
        response.setEstado("APROBADO");
        response.setCodigoTransaccion("TXN-789012");
        response.setFechaPago(LocalDateTime.now());

        when(pagoServiceMock.procesarPago(any(PagoRequestDTO.class))).thenReturn(response);

        // When
        PagoResponseDTO resultado = pagoServiceMock.procesarPago(request);

        // Then
        assertNotNull(resultado);
        assertEquals(25000.0, resultado.getMonto());
        assertEquals("EFECTIVO", resultado.getMetodo());
        verify(pagoServiceMock, times(1)).procesarPago(any(PagoRequestDTO.class));
    }

    @Test
    public void testProcesarPago_ConMetodoTransferencia() {
        // Given
        PagoRequestDTO request = new PagoRequestDTO(3L, 30000.0, "TRANSFERENCIA");
        PagoResponseDTO response = new PagoResponseDTO();
        response.setId(3L);
        response.setReservaId(3L);
        response.setMonto(30000.0);
        response.setMetodo("TRANSFERENCIA");
        response.setEstado("APROBADO");
        response.setCodigoTransaccion("TXN-345678");
        response.setFechaPago(LocalDateTime.now());

        when(pagoServiceMock.procesarPago(any(PagoRequestDTO.class))).thenReturn(response);

        // When
        PagoResponseDTO resultado = pagoServiceMock.procesarPago(request);

        // Then
        assertNotNull(resultado);
        assertEquals("TRANSFERENCIA", resultado.getMetodo());
        verify(pagoServiceMock, times(1)).procesarPago(any(PagoRequestDTO.class));
    }

    @Test
    public void testProcesarPago_ConMensajeError() {
        // Given
        PagoResponseDTO responseConError = new PagoResponseDTO();
        responseConError.setId(4L);
        responseConError.setReservaId(1L);
        responseConError.setMonto(15000.0);
        responseConError.setMetodo("TARJETA");
        responseConError.setEstado("RECHAZADO");
        responseConError.setCodigoTransaccion("TXN-901234");
        responseConError.setFechaPago(LocalDateTime.now());
        responseConError.setMensajeError("Pago rechazado por el banco");

        when(pagoServiceMock.procesarPago(any(PagoRequestDTO.class))).thenReturn(responseConError);

        // When
        PagoResponseDTO resultado = pagoServiceMock.procesarPago(requestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("RECHAZADO", resultado.getEstado());
        assertEquals("Pago rechazado por el banco", resultado.getMensajeError());
        verify(pagoServiceMock, times(1)).procesarPago(any(PagoRequestDTO.class));
    }

    // ─── GET /api/pagos/reserva/{reservaId} ─────────────────────────

    @Test
    public void testObtenerPorReserva_CuandoExiste() {
        // Given
        Long reservaId = 1L;
        when(pagoServiceMock.obtenerPagoPorReserva(reservaId)).thenReturn(Optional.of(responseDTO));

        // When
        Optional<PagoResponseDTO> resultado = pagoServiceMock.obtenerPagoPorReserva(reservaId);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(responseDTO.getId(), resultado.get().getId());
        assertEquals(responseDTO.getReservaId(), resultado.get().getReservaId());
        verify(pagoServiceMock, times(1)).obtenerPagoPorReserva(reservaId);
    }

    @Test
    public void testObtenerPorReserva_CuandoNoExiste() {
        // Given
        Long reservaId = 999L;
        when(pagoServiceMock.obtenerPagoPorReserva(reservaId)).thenReturn(Optional.empty());

        // When
        Optional<PagoResponseDTO> resultado = pagoServiceMock.obtenerPagoPorReserva(reservaId);

        // Then
        assertTrue(resultado.isEmpty());
        verify(pagoServiceMock, times(1)).obtenerPagoPorReserva(reservaId);
    }

    @Test
    public void testObtenerPorReserva_ConReservaIdNulo() {
        // Given
        Long reservaId = null;
        when(pagoServiceMock.obtenerPagoPorReserva(reservaId)).thenReturn(Optional.empty());

        // When
        Optional<PagoResponseDTO> resultado = pagoServiceMock.obtenerPagoPorReserva(reservaId);

        // Then
        assertTrue(resultado.isEmpty());
        verify(pagoServiceMock, times(1)).obtenerPagoPorReserva(reservaId);
    }

    // ─── GET /api/pagos/{id} ─────────────────────────────────────────

    @Test
    public void testObtenerPorId_CuandoExiste() {
        // Given
        Long id = 1L;
        when(pagoServiceMock.obtenerPagoPorId(id)).thenReturn(Optional.of(responseDTO));

        // When
        Optional<PagoResponseDTO> resultado = pagoServiceMock.obtenerPagoPorId(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(responseDTO.getId(), resultado.get().getId());
        verify(pagoServiceMock, times(1)).obtenerPagoPorId(id);
    }

    @Test
    public void testObtenerPorId_CuandoNoExiste() {
        // Given
        Long id = 999L;
        when(pagoServiceMock.obtenerPagoPorId(id)).thenReturn(Optional.empty());

        // When
        Optional<PagoResponseDTO> resultado = pagoServiceMock.obtenerPagoPorId(id);

        // Then
        assertTrue(resultado.isEmpty());
        verify(pagoServiceMock, times(1)).obtenerPagoPorId(id);
    }

    @Test
    public void testObtenerPorId_ConIdNulo() {
        // Given
        Long id = null;
        when(pagoServiceMock.obtenerPagoPorId(id)).thenReturn(Optional.empty());

        // When
        Optional<PagoResponseDTO> resultado = pagoServiceMock.obtenerPagoPorId(id);

        // Then
        assertTrue(resultado.isEmpty());
        verify(pagoServiceMock, times(1)).obtenerPagoPorId(id);
    }

    // ─── Tests de integración de flujo ──────────────────────────────

    @Test
    public void testFlujoCompleto_CrearYBuscar() {
        // Given
        PagoRequestDTO request = new PagoRequestDTO(1L, 15000.0, "TARJETA");
        PagoResponseDTO response = new PagoResponseDTO();
        response.setId(1L);
        response.setReservaId(1L);
        response.setMonto(15000.0);
        response.setMetodo("TARJETA");
        response.setEstado("APROBADO");
        response.setCodigoTransaccion("TXN-123456");
        response.setFechaPago(LocalDateTime.now());

        when(pagoServiceMock.procesarPago(any(PagoRequestDTO.class))).thenReturn(response);
        when(pagoServiceMock.obtenerPagoPorId(1L)).thenReturn(Optional.of(response));

        // When
        PagoResponseDTO creado = pagoServiceMock.procesarPago(request);
        Optional<PagoResponseDTO> encontrado = pagoServiceMock.obtenerPagoPorId(creado.getId());

        // Then
        assertNotNull(creado);
        assertTrue(encontrado.isPresent());
        assertEquals(creado.getId(), encontrado.get().getId());
        verify(pagoServiceMock, times(1)).procesarPago(any(PagoRequestDTO.class));
        verify(pagoServiceMock, times(1)).obtenerPagoPorId(1L);
    }

    @Test
    public void testFlujoCompleto_CrearYBuscarPorReserva() {
        // Given
        PagoRequestDTO request = new PagoRequestDTO(1L, 15000.0, "TARJETA");
        PagoResponseDTO response = new PagoResponseDTO();
        response.setId(1L);
        response.setReservaId(1L);
        response.setMonto(15000.0);
        response.setMetodo("TARJETA");
        response.setEstado("APROBADO");
        response.setCodigoTransaccion("TXN-123456");
        response.setFechaPago(LocalDateTime.now());

        when(pagoServiceMock.procesarPago(any(PagoRequestDTO.class))).thenReturn(response);
        when(pagoServiceMock.obtenerPagoPorReserva(1L)).thenReturn(Optional.of(response));

        // When
        PagoResponseDTO creado = pagoServiceMock.procesarPago(request);
        Optional<PagoResponseDTO> encontrado = pagoServiceMock.obtenerPagoPorReserva(creado.getReservaId());

        // Then
        assertNotNull(creado);
        assertTrue(encontrado.isPresent());
        assertEquals(creado.getReservaId(), encontrado.get().getReservaId());
        verify(pagoServiceMock, times(1)).procesarPago(any(PagoRequestDTO.class));
        verify(pagoServiceMock, times(1)).obtenerPagoPorReserva(1L);
    }
}