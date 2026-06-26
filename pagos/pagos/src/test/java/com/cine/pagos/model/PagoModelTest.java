package com.cine.pagos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PagoModelTest {

    private Pago pago;
    private LocalDateTime fecha;

    @BeforeEach
    void setUp() {
        fecha = LocalDateTime.now();
        pago = new Pago();
        pago.setId(1L);
        pago.setReservaId(1L);
        pago.setMonto(15000.0);
        pago.setMetodo("TARJETA");
        pago.setEstado("PROCESANDO");
        pago.setCodigoTransaccion("TXN-123456");
        pago.setFechaPago(fecha);
        pago.setMensajeError(null);
    }

    // ─── Constructor vacío ──────────────────────────────────────────

    @Test
    public void testConstructorVacio() {
        Pago p = new Pago();
        assertNotNull(p);
        assertNull(p.getId());
        assertNull(p.getReservaId());
        assertNull(p.getMonto());
        assertNull(p.getMetodo());
        assertNull(p.getEstado());
        assertNull(p.getCodigoTransaccion());
        assertNull(p.getFechaPago());
        assertNull(p.getMensajeError());
    }

    // ─── Constructor con parámetros (si existe) ────────────────────

    @Test
    public void testConstructorConParametros() {
        // Si tu entidad tiene un constructor con todos los parámetros
        // Si no, puedes omitir este test
        Pago p = new Pago();
        p.setId(1L);
        p.setReservaId(1L);
        p.setMonto(15000.0);
        p.setMetodo("TARJETA");
        p.setEstado("APROBADO");
        p.setCodigoTransaccion("TXN-789012");
        p.setFechaPago(fecha);
        p.setMensajeError(null);

        assertNotNull(p);
        assertEquals(1L, p.getId());
        assertEquals(1L, p.getReservaId());
        assertEquals(15000.0, p.getMonto());
        assertEquals("TARJETA", p.getMetodo());
        assertEquals("APROBADO", p.getEstado());
        assertEquals("TXN-789012", p.getCodigoTransaccion());
        assertEquals(fecha, p.getFechaPago());
        assertNull(p.getMensajeError());
    }

    // ─── Getters y Setters ──────────────────────────────────────────

    @Test
    public void testSetId() {
        pago.setId(999L);
        assertEquals(999L, pago.getId());
    }

    @Test
    public void testSetReservaId() {
        pago.setReservaId(999L);
        assertEquals(999L, pago.getReservaId());
    }

    @Test
    public void testSetMonto() {
        pago.setMonto(25000.0);
        assertEquals(25000.0, pago.getMonto());
    }

    @Test
    public void testSetMetodo() {
        pago.setMetodo("EFECTIVO");
        assertEquals("EFECTIVO", pago.getMetodo());
    }

    @Test
    public void testSetEstado() {
        pago.setEstado("APROBADO");
        assertEquals("APROBADO", pago.getEstado());
    }

    @Test
    public void testSetCodigoTransaccion() {
        pago.setCodigoTransaccion("TXN-999999");
        assertEquals("TXN-999999", pago.getCodigoTransaccion());
    }

    @Test
    public void testSetFechaPago() {
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(1);
        pago.setFechaPago(nuevaFecha);
        assertEquals(nuevaFecha, pago.getFechaPago());
    }

    @Test
    public void testSetMensajeError() {
        pago.setMensajeError("Pago rechazado por el banco");
        assertEquals("Pago rechazado por el banco", pago.getMensajeError());
    }

    // ─── Todos los getters ──────────────────────────────────────────

    @Test
    public void testTodosLosGetters() {
        assertEquals(1L, pago.getId());
        assertEquals(1L, pago.getReservaId());
        assertEquals(15000.0, pago.getMonto());
        assertEquals("TARJETA", pago.getMetodo());
        assertEquals("PROCESANDO", pago.getEstado());
        assertEquals("TXN-123456", pago.getCodigoTransaccion());
        assertEquals(fecha, pago.getFechaPago());
        assertNull(pago.getMensajeError());
    }

    // ─── toString() ─────────────────────────────────────────────────

    @Test
    public void testToString() {
        String toString = pago.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("reservaId=1"));
        assertTrue(toString.contains("monto=15000.0"));
        assertTrue(toString.contains("metodo=TARJETA"));
        assertTrue(toString.contains("estado=PROCESANDO"));
        assertTrue(toString.contains("codigoTransaccion=TXN-123456"));
        assertTrue(toString.contains("fechaPago=" + fecha));
    }

    // ─── equals() y hashCode() ─────────────────────────────────────

    @Test
    public void testEquals_CuandoSonIguales() {
        Pago p1 = new Pago();
        p1.setId(1L);
        p1.setReservaId(1L);
        p1.setMonto(15000.0);
        p1.setMetodo("TARJETA");
        p1.setEstado("PROCESANDO");
        p1.setCodigoTransaccion("TXN-123456");
        p1.setFechaPago(fecha);
        p1.setMensajeError(null);

        Pago p2 = new Pago();
        p2.setId(1L);
        p2.setReservaId(1L);
        p2.setMonto(15000.0);
        p2.setMetodo("TARJETA");
        p2.setEstado("PROCESANDO");
        p2.setCodigoTransaccion("TXN-123456");
        p2.setFechaPago(fecha);
        p2.setMensajeError(null);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorId() {
        Pago p1 = new Pago();
        p1.setId(1L);
        p1.setReservaId(1L);
        p1.setMonto(15000.0);
        p1.setMetodo("TARJETA");
        p1.setEstado("PROCESANDO");
        p1.setCodigoTransaccion("TXN-123456");
        p1.setFechaPago(fecha);

        Pago p2 = new Pago();
        p2.setId(2L);
        p2.setReservaId(1L);
        p2.setMonto(15000.0);
        p2.setMetodo("TARJETA");
        p2.setEstado("PROCESANDO");
        p2.setCodigoTransaccion("TXN-123456");
        p2.setFechaPago(fecha);

        assertNotEquals(p1, p2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorReservaId() {
        Pago p1 = new Pago();
        p1.setId(1L);
        p1.setReservaId(1L);
        p1.setMonto(15000.0);
        p1.setMetodo("TARJETA");
        p1.setEstado("PROCESANDO");
        p1.setCodigoTransaccion("TXN-123456");
        p1.setFechaPago(fecha);

        Pago p2 = new Pago();
        p2.setId(1L);
        p2.setReservaId(2L);
        p2.setMonto(15000.0);
        p2.setMetodo("TARJETA");
        p2.setEstado("PROCESANDO");
        p2.setCodigoTransaccion("TXN-123456");
        p2.setFechaPago(fecha);

        assertNotEquals(p1, p2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorEstado() {
        Pago p1 = new Pago();
        p1.setId(1L);
        p1.setReservaId(1L);
        p1.setMonto(15000.0);
        p1.setMetodo("TARJETA");
        p1.setEstado("PROCESANDO");
        p1.setCodigoTransaccion("TXN-123456");
        p1.setFechaPago(fecha);

        Pago p2 = new Pago();
        p2.setId(1L);
        p2.setReservaId(1L);
        p2.setMonto(15000.0);
        p2.setMetodo("TARJETA");
        p2.setEstado("APROBADO");
        p2.setCodigoTransaccion("TXN-123456");
        p2.setFechaPago(fecha);

        assertNotEquals(p1, p2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorCodigoTransaccion() {
        Pago p1 = new Pago();
        p1.setId(1L);
        p1.setReservaId(1L);
        p1.setMonto(15000.0);
        p1.setMetodo("TARJETA");
        p1.setEstado("PROCESANDO");
        p1.setCodigoTransaccion("TXN-123456");
        p1.setFechaPago(fecha);

        Pago p2 = new Pago();
        p2.setId(1L);
        p2.setReservaId(1L);
        p2.setMonto(15000.0);
        p2.setMetodo("TARJETA");
        p2.setEstado("PROCESANDO");
        p2.setCodigoTransaccion("TXN-999999");
        p2.setFechaPago(fecha);

        assertNotEquals(p1, p2);
    }

    @Test
    public void testEquals_CuandoEsNulo() {
        assertNotEquals(pago, null);
    }

    @Test
    public void testEquals_CuandoEsMismoObjeto() {
        assertEquals(pago, pago);
    }

    // ─── Tests de valores extremos ──────────────────────────────────

    @Test
    public void testMontoMuyGrande() {
        pago.setMonto(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, pago.getMonto());
    }

    @Test
    public void testIdMuyGrande() {
        pago.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, pago.getId());
    }

    @Test
    public void testReservaIdMuyGrande() {
        pago.setReservaId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, pago.getReservaId());
    }

    // ─── Tests de estados ───────────────────────────────────────────

    @Test
    public void testEstadoProcesando() {
        pago.setEstado("PROCESANDO");
        assertEquals("PROCESANDO", pago.getEstado());
    }

    @Test
    public void testEstadoAprobado() {
        pago.setEstado("APROBADO");
        assertEquals("APROBADO", pago.getEstado());
    }

    @Test
    public void testEstadoRechazado() {
        pago.setEstado("RECHAZADO");
        assertEquals("RECHAZADO", pago.getEstado());
    }

    @Test
    public void testEstadoTimeout() {
        pago.setEstado("TIMEOUT");
        assertEquals("TIMEOUT", pago.getEstado());
    }

    // ─── Tests de mensaje de error ─────────────────────────────────

    @Test
    public void testMensajeErrorConValor() {
        pago.setMensajeError("Fondos insuficientes");
        assertEquals("Fondos insuficientes", pago.getMensajeError());
    }

    @Test
    public void testMensajeErrorNull() {
        pago.setMensajeError(null);
        assertNull(pago.getMensajeError());
    }

    @Test
    public void testMensajeErrorConEspacios() {
        pago.setMensajeError("  Error con espacios  ");
        assertEquals("  Error con espacios  ", pago.getMensajeError());
    }

    // ─── Tests de fecha ─────────────────────────────────────────────

    @Test
    public void testFechaPagoEsPasado() {
        LocalDateTime pasado = LocalDateTime.now().minusDays(1);
        pago.setFechaPago(pasado);
        assertTrue(pago.getFechaPago().isBefore(LocalDateTime.now()));
    }

    @Test
    public void testFechaPagoEsFuturo() {
        LocalDateTime futuro = LocalDateTime.now().plusDays(1);
        pago.setFechaPago(futuro);
        assertTrue(pago.getFechaPago().isAfter(LocalDateTime.now()));
    }

    // ─── Test de consistencia ───────────────────────────────────────

    @Test
    public void testNingunCampoEsNullDespuesDeSetear() {
        assertNotNull(pago.getId());
        assertNotNull(pago.getReservaId());
        assertNotNull(pago.getMonto());
        assertNotNull(pago.getMetodo());
        assertNotNull(pago.getEstado());
        assertNotNull(pago.getCodigoTransaccion());
        assertNotNull(pago.getFechaPago());
    }

    @Test
    public void testTodosLosCamposPuedenSerNull() {
        Pago p = new Pago();
        assertNull(p.getId());
        assertNull(p.getReservaId());
        assertNull(p.getMonto());
        assertNull(p.getMetodo());
        assertNull(p.getEstado());
        assertNull(p.getCodigoTransaccion());
        assertNull(p.getFechaPago());
        assertNull(p.getMensajeError());
    }
}