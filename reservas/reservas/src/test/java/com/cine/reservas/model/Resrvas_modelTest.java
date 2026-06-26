package com.cine.reservas.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class Resrvas_modelTest {

    private Resrvas_model reserva;
    private LocalDateTime fecha;

    @BeforeEach
    void setUp() {
        fecha = LocalDateTime.now();
        reserva = new Resrvas_model();
        reserva.setId(1L);
        reserva.setUsuarioId(1L);
        reserva.setFuncionId(1L);
        reserva.setCantidadDeAsientos(2);
        reserva.setTotal(15000.0);
        reserva.setEstado("PENDIENTE");
        reserva.setFechaReserva(fecha);
    }

    @Test
    public void testConstructorVacio() {
        Resrvas_model r = new Resrvas_model();
        assertNotNull(r);
        assertEquals(0L, r.getId());
        assertEquals(0L, r.getUsuarioId());
        assertEquals(0L, r.getFuncionId());
        assertNull(r.getCantidadDeAsientos());
        assertNull(r.getTotal());
        assertNull(r.getEstado());
        assertNull(r.getFechaReserva());
    }

    @Test
    public void testConstructorConParametros() {
        Resrvas_model r = new Resrvas_model(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);

        assertNotNull(r);
        assertEquals(1L, r.getId());
        assertEquals(1L, r.getUsuarioId());
        assertEquals(1L, r.getFuncionId());
        assertEquals(2, r.getCantidadDeAsientos());
        assertEquals(15000.0, r.getTotal());
        assertEquals("CONFIRMADA", r.getEstado());
        assertEquals(fecha, r.getFechaReserva());
    }

    @Test
    public void testSetId() {
        reserva.setId(999L);
        assertEquals(999L, reserva.getId());
    }

    @Test
    public void testSetUsuarioId() {
        reserva.setUsuarioId(999L);
        assertEquals(999L, reserva.getUsuarioId());
    }

    @Test
    public void testSetFuncionId() {
        reserva.setFuncionId(999L);
        assertEquals(999L, reserva.getFuncionId());
    }

    @Test
    public void testSetCantidadDeAsientos() {
        reserva.setCantidadDeAsientos(5);
        assertEquals(5, reserva.getCantidadDeAsientos());
    }

    @Test
    public void testSetTotal() {
        reserva.setTotal(25000.0);
        assertEquals(25000.0, reserva.getTotal());
    }

    @Test
    public void testSetEstado() {
        reserva.setEstado("CANCELADA");
        assertEquals("CANCELADA", reserva.getEstado());
    }

    @Test
    public void testSetFechaReserva() {
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(1);
        reserva.setFechaReserva(nuevaFecha);
        assertEquals(nuevaFecha, reserva.getFechaReserva());
    }

    @Test
    public void testGetAllGetters() {
        assertEquals(1L, reserva.getId());
        assertEquals(1L, reserva.getUsuarioId());
        assertEquals(1L, reserva.getFuncionId());
        assertEquals(2, reserva.getCantidadDeAsientos());
        assertEquals(15000.0, reserva.getTotal());
        assertEquals("PENDIENTE", reserva.getEstado());
        assertEquals(fecha, reserva.getFechaReserva());
    }

    @Test
    public void testToString() {
        String toString = reserva.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("usuarioId=1"));
        assertTrue(toString.contains("funcionId=1"));
        assertTrue(toString.contains("cantidadDeAsientos=2"));
        assertTrue(toString.contains("total=15000.0"));
        assertTrue(toString.contains("estado=PENDIENTE"));
    }

    @Test
    public void testEquals_CuandoSonIguales() {
        Resrvas_model r1 = new Resrvas_model(1L, 1L, 1L, 2, 15000.0, "PENDIENTE", fecha);
        Resrvas_model r2 = new Resrvas_model(1L, 1L, 1L, 2, 15000.0, "PENDIENTE", fecha);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorId() {
        Resrvas_model r1 = new Resrvas_model(1L, 1L, 1L, 2, 15000.0, "PENDIENTE", fecha);
        Resrvas_model r2 = new Resrvas_model(2L, 1L, 1L, 2, 15000.0, "PENDIENTE", fecha);

        assertNotEquals(r1, r2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorEstado() {
        Resrvas_model r1 = new Resrvas_model(1L, 1L, 1L, 2, 15000.0, "PENDIENTE", fecha);
        Resrvas_model r2 = new Resrvas_model(1L, 1L, 1L, 2, 15000.0, "CONFIRMADA", fecha);

        assertNotEquals(r1, r2);
    }

    @Test
    public void testEquals_CuandoEsNulo() {
        assertNotEquals(reserva, null);
    }

    @Test
    public void testEquals_CuandoEsMismoObjeto() {
        assertEquals(reserva, reserva);
    }

    @Test
    public void testCamposIniciales() {
        Resrvas_model r = new Resrvas_model();
        assertEquals(0L, r.getId());
        assertEquals(0L, r.getUsuarioId());
        assertEquals(0L, r.getFuncionId());
        assertNull(r.getCantidadDeAsientos());
        assertNull(r.getTotal());
        assertNull(r.getEstado());
        assertNull(r.getFechaReserva());
    }
}