package com.cine.reservas.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaResponseDTOTest {

    private ReservaResponseDTO dto;
    private LocalDateTime fecha;

    @BeforeEach
    void setUp() {
        fecha = LocalDateTime.now();
        dto = new ReservaResponseDTO();
        dto.setId(1L);
        dto.setUsuario_id(1L);
        dto.setFuncion_id(1L);
        dto.setCantidad_de_asientos(2);
        dto.setTotal(15000.0);
        dto.setEstado("CONFIRMADA");
        dto.setFechaReserva(fecha);
    }

    // ─── Constructor vacío ──────────────────────────────────────────

    @Test
    public void testConstructorVacio() {
        ReservaResponseDTO dtoVacio = new ReservaResponseDTO();
        assertNotNull(dtoVacio);
        assertNull(dtoVacio.getId());
        assertNull(dtoVacio.getUsuario_id());
        assertNull(dtoVacio.getFuncion_id());
        assertNull(dtoVacio.getCantidad_de_asientos());
        assertNull(dtoVacio.getTotal());
        assertNull(dtoVacio.getEstado());
        assertNull(dtoVacio.getFechaReserva());
    }

    // ─── Constructor con parámetros ─────────────────────────────────

    @Test
    public void testConstructorConParametros() {
        ReservaResponseDTO dtoConParams = new ReservaResponseDTO(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);

        assertEquals(1L, dtoConParams.getId());
        assertEquals(1L, dtoConParams.getUsuario_id());
        assertEquals(1L, dtoConParams.getFuncion_id());
        assertEquals(2, dtoConParams.getCantidad_de_asientos());
        assertEquals(15000.0, dtoConParams.getTotal());
        assertEquals("CONFIRMADA", dtoConParams.getEstado());
        assertEquals(fecha, dtoConParams.getFechaReserva());
    }

    // ─── Getters y Setters ──────────────────────────────────────────

    @Test
    public void testSetId() {
        dto.setId(999L);
        assertEquals(999L, dto.getId());
    }

    @Test
    public void testSetUsuario_id() {
        dto.setUsuario_id(999L);
        assertEquals(999L, dto.getUsuario_id());
    }

    @Test
    public void testSetFuncion_id() {
        dto.setFuncion_id(999L);
        assertEquals(999L, dto.getFuncion_id());
    }

    @Test
    public void testSetCantidad_de_asientos() {
        dto.setCantidad_de_asientos(5);
        assertEquals(5, dto.getCantidad_de_asientos());
    }

    @Test
    public void testSetTotal() {
        dto.setTotal(25000.0);
        assertEquals(25000.0, dto.getTotal());
    }

    @Test
    public void testSetEstado() {
        dto.setEstado("CANCELADA");
        assertEquals("CANCELADA", dto.getEstado());
    }

    @Test
    public void testSetFechaReserva() {
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(1);
        dto.setFechaReserva(nuevaFecha);
        assertEquals(nuevaFecha, dto.getFechaReserva());
    }

    // ─── Todos los getters ──────────────────────────────────────────

    @Test
    public void testTodosLosGetters() {
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getUsuario_id());
        assertEquals(1L, dto.getFuncion_id());
        assertEquals(2, dto.getCantidad_de_asientos());
        assertEquals(15000.0, dto.getTotal());
        assertEquals("CONFIRMADA", dto.getEstado());
        assertEquals(fecha, dto.getFechaReserva());
    }

    // ─── toString() ─────────────────────────────────────────────────

    @Test
    public void testToString() {
        String toString = dto.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("usuario_id=1"));
        assertTrue(toString.contains("funcion_id=1"));
        assertTrue(toString.contains("cantidad_de_asientos=2"));
        assertTrue(toString.contains("total=15000.0"));
        assertTrue(toString.contains("estado=CONFIRMADA"));
        assertTrue(toString.contains("fechaReserva=" + fecha));
    }

    // ─── equals() y hashCode() ─────────────────────────────────────

    @Test
    public void testEquals_CuandoSonIguales() {
        ReservaResponseDTO dto1 = new ReservaResponseDTO(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);
        ReservaResponseDTO dto2 = new ReservaResponseDTO(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorId() {
        ReservaResponseDTO dto1 = new ReservaResponseDTO(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);
        ReservaResponseDTO dto2 = new ReservaResponseDTO(2L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorUsuarioId() {
        ReservaResponseDTO dto1 = new ReservaResponseDTO(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);
        ReservaResponseDTO dto2 = new ReservaResponseDTO(1L, 2L, 1L, 2, 15000.0, "CONFIRMADA", fecha);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorFuncionId() {
        ReservaResponseDTO dto1 = new ReservaResponseDTO(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);
        ReservaResponseDTO dto2 = new ReservaResponseDTO(1L, 1L, 2L, 2, 15000.0, "CONFIRMADA", fecha);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorCantidad() {
        ReservaResponseDTO dto1 = new ReservaResponseDTO(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);
        ReservaResponseDTO dto2 = new ReservaResponseDTO(1L, 1L, 1L, 3, 15000.0, "CONFIRMADA", fecha);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorTotal() {
        ReservaResponseDTO dto1 = new ReservaResponseDTO(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);
        ReservaResponseDTO dto2 = new ReservaResponseDTO(1L, 1L, 1L, 2, 20000.0, "CONFIRMADA", fecha);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorEstado() {
        ReservaResponseDTO dto1 = new ReservaResponseDTO(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);
        ReservaResponseDTO dto2 = new ReservaResponseDTO(1L, 1L, 1L, 2, 15000.0, "PENDIENTE", fecha);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorFecha() {
        LocalDateTime otraFecha = LocalDateTime.now().plusDays(1);
        ReservaResponseDTO dto1 = new ReservaResponseDTO(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);
        ReservaResponseDTO dto2 = new ReservaResponseDTO(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", otraFecha);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoEsNulo() {
        assertNotEquals(dto, null);
    }

    @Test
    public void testEquals_CuandoEsMismoObjeto() {
        assertEquals(dto, dto);
    }

    // ─── Tests de valores extremos ──────────────────────────────────

    @Test
    public void testCantidadDeAsientosMuyGrande() {
        dto.setCantidad_de_asientos(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, dto.getCantidad_de_asientos());
    }

    @Test
    public void testTotalMuyGrande() {
        dto.setTotal(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, dto.getTotal());
    }

    @Test
    public void testIdMuyGrande() {
        dto.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, dto.getId());
    }

    @Test
    public void testUsuarioIdMuyGrande() {
        dto.setUsuario_id(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, dto.getUsuario_id());
    }

    @Test
    public void testFuncionIdMuyGrande() {
        dto.setFuncion_id(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, dto.getFuncion_id());
    }

    // ─── Tests de estados ───────────────────────────────────────────

    @Test
    public void testEstadoPendiente() {
        dto.setEstado("PENDIENTE");
        assertEquals("PENDIENTE", dto.getEstado());
    }

    @Test
    public void testEstadoCancelada() {
        dto.setEstado("CANCELADA");
        assertEquals("CANCELADA", dto.getEstado());
    }

    @Test
    public void testEstadoConfirmada() {
        dto.setEstado("CONFIRMADA");
        assertEquals("CONFIRMADA", dto.getEstado());
    }

    @Test
    public void testEstadoConLetrasMinusculas() {
        dto.setEstado("confirmada");
        assertEquals("confirmada", dto.getEstado());
    }

    // ─── Tests de fecha ─────────────────────────────────────────────

    @Test
    public void testFechaReservaEsPasado() {
        LocalDateTime pasado = LocalDateTime.now().minusDays(1);
        dto.setFechaReserva(pasado);
        assertTrue(dto.getFechaReserva().isBefore(LocalDateTime.now()));
    }

    @Test
    public void testFechaReservaEsFuturo() {
        LocalDateTime futuro = LocalDateTime.now().plusDays(1);
        dto.setFechaReserva(futuro);
        assertTrue(dto.getFechaReserva().isAfter(LocalDateTime.now()));
    }

    // ─── Test de consistencia ───────────────────────────────────────

    @Test
    public void testNingunCampoEsNullDespuesDeSetear() {
        assertNotNull(dto.getId());
        assertNotNull(dto.getUsuario_id());
        assertNotNull(dto.getFuncion_id());
        assertNotNull(dto.getCantidad_de_asientos());
        assertNotNull(dto.getTotal());
        assertNotNull(dto.getEstado());
        assertNotNull(dto.getFechaReserva());
    }

    @Test
    public void testTodosLosCamposPuedenSerNull() {
        ReservaResponseDTO dtoNull = new ReservaResponseDTO();
        assertNull(dtoNull.getId());
        assertNull(dtoNull.getUsuario_id());
        assertNull(dtoNull.getFuncion_id());
        assertNull(dtoNull.getCantidad_de_asientos());
        assertNull(dtoNull.getTotal());
        assertNull(dtoNull.getEstado());
        assertNull(dtoNull.getFechaReserva());
    }
}