package com.cine.reservas.controller;

import com.cine.reservas.dto.ReservaRequestDTO;
import com.cine.reservas.dto.ReservaResponseDTO;
import com.cine.reservas.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ReservaControllerTest {

    @Autowired
    private ReservaService reservaService;

    @MockitoBean
    private ReservaService reservaServiceMock;

    private ReservaRequestDTO requestDTO;
    private ReservaResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new ReservaRequestDTO();
        requestDTO.setUsuario_id(1L);
        requestDTO.setFuncion_id(1L);
        requestDTO.setCantidad_de_asientos(2);
        requestDTO.setTotal(15000.0);

        responseDTO = new ReservaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setUsuario_id(1L);
        responseDTO.setFuncion_id(1L);
        responseDTO.setCantidad_de_asientos(2);
        responseDTO.setTotal(15000.0);
        responseDTO.setEstado("CONFIRMADA");
        responseDTO.setFechaReserva(LocalDateTime.now());
    }

    @Test
    public void testObtenerTodas() {
        when(reservaServiceMock.obtenerTodas()).thenReturn(List.of(responseDTO));

        List<ReservaResponseDTO> resultado = reservaServiceMock.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(reservaServiceMock, times(1)).obtenerTodas();
    }

    @Test
    public void testObtenerPorId() {
        when(reservaServiceMock.obtenerPorId(1L)).thenReturn(Optional.of(responseDTO));

        Optional<ReservaResponseDTO> resultado = reservaServiceMock.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        verify(reservaServiceMock, times(1)).obtenerPorId(1L);
    }

    @Test
    public void testObtenerPorId_NotFound() {
        when(reservaServiceMock.obtenerPorId(999L)).thenReturn(Optional.empty());

        Optional<ReservaResponseDTO> resultado = reservaServiceMock.obtenerPorId(999L);

        assertTrue(resultado.isEmpty());
        verify(reservaServiceMock, times(1)).obtenerPorId(999L);
    }

    @Test
    public void testGuardar() {
        when(reservaServiceMock.guardar(any(ReservaRequestDTO.class))).thenReturn(responseDTO);

        ReservaResponseDTO resultado = reservaServiceMock.guardar(requestDTO);

        assertNotNull(resultado);
        verify(reservaServiceMock, times(1)).guardar(any(ReservaRequestDTO.class));
    }

    @Test
    public void testCancelar() {
        when(reservaServiceMock.cancelar(1L)).thenReturn(Optional.of(responseDTO));

        Optional<ReservaResponseDTO> resultado = reservaServiceMock.cancelar(1L);

        assertTrue(resultado.isPresent());
        verify(reservaServiceMock, times(1)).cancelar(1L);
    }

    @Test
    public void testConfirmar() {
        when(reservaServiceMock.confirmar(1L)).thenReturn(Optional.of(responseDTO));

        Optional<ReservaResponseDTO> resultado = reservaServiceMock.confirmar(1L);

        assertTrue(resultado.isPresent());
        verify(reservaServiceMock, times(1)).confirmar(1L);
    }

    @Test
    public void testEliminar() {
        doNothing().when(reservaServiceMock).eliminar(1L);

        reservaServiceMock.eliminar(1L);

        verify(reservaServiceMock, times(1)).eliminar(1L);
    }
}