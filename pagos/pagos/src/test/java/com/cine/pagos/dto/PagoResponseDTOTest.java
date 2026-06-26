package com.cine.pagos.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PagoResponseDTOTest {

    private PagoResponseDTO dto;
    private LocalDateTime fecha;

    @BeforeEach
    void setUp() {
        fecha = LocalDateTime.now();
        dto = new PagoResponseDTO();
        dto.setId(1L);
        dto.setReservaId(1L);
        dto.setMonto(15000.0);
        dto.setMetodo("TARJETA");
        dto.setEstado("APROBADO");
        dto.setCodigoTransaccion("TXN-123456");
        dto.setFechaPago(fecha);
        dto.setMensajeError(null);
    }

    // ─── Constructor vacío ──────────────────────────────────────────

    @Test
    public void testConstructorVacio() {
        PagoResponseDTO dtoVacio = new PagoResponseDTO();
        assertNotNull(dtoVacio);
        assertNull(dtoVacio.getId());
        assertNull(dtoVacio.getReservaId());
        assertNull(dtoVacio.getMonto());
        assertNull(dtoVacio.getMetodo());
        assertNull(dtoVacio.getEstado());
        assertNull(dtoVacio.getCodigoTransaccion());
        assertNull(dtoVacio.getFechaPago());
        assertNull(dtoVacio.getMensajeError());
    }

    // ─── Constructor con parámetros ─────────────────────────────────

    @Test
    public void testConstructorConParametros() {
        PagoResponseDTO dtoConParams = new PagoResponseDTO(
                1L, 1L, 15000.0, "TARJETA", "APROBADO", "TXN-123456", fecha, null
        );

        assertEquals(1L, dtoConParams.getId());
        assertEquals(1L, dtoConParams.getReservaId());
        assertEquals(15000.0, dtoConParams.getMonto());
        assertEquals("TARJETA", dtoConParams.getMetodo());
        assertEquals("APROBADO", dtoConParams.getEstado());
        assertEquals("TXN-123456", dtoConParams.getCodigoTransaccion());
        assertEquals(fecha, dtoConParams.getFechaPago());
        assertNull(dtoConParams.getMensajeError());
    }

    @Test
    public void testConstructorConParametros_ConMensajeError() {
        PagoResponseDTO dtoConParams = new PagoResponseDTO(
                2L, 2L, 20000.0, "EFECTIVO", "RECHAZADO", "TXN-789012", fecha, "Fondos insuficientes"
        );

        assertEquals(2L, dtoConParams.getId());
        assertEquals("RECHAZADO", dtoConParams.getEstado());
        assertEquals("Fondos insuficientes", dtoConParams.getMensajeError());
    }

    // ─── Getters y Setters ──────────────────────────────────────────

    @Test
    public void testSetId() {
        dto.setId(999L);
        assertEquals(999L, dto.getId());
    }

    @Test
    public void testSetReservaId() {
        dto.setReservaId(999L);
        assertEquals(999L, dto.getReservaId());
    }

    @Test
    public void testSetMonto() {
        dto.setMonto(25000.0);
        assertEquals(25000.0, dto.getMonto());
    }

    @Test
    public void testSetMetodo() {
        dto.setMetodo("EFECTIVO");
        assertEquals("EFECTIVO", dto.getMetodo());
    }

    @Test
    public void testSetEstado() {
        dto.setEstado("RECHAZADO");
        assertEquals("RECHAZADO", dto.getEstado());
    }

    @Test
    public void testSetCodigoTransaccion() {
        dto.setCodigoTransaccion("TXN-999999");
        assertEquals("TXN-999999", dto.getCodigoTransaccion());
    }

    @Test
    public void testSetFechaPago() {
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(1);
        dto.setFechaPago(nuevaFecha);
        assertEquals(nuevaFecha, dto.getFechaPago());
    }

    @Test
    public void testSetMensajeError() {
        dto.setMensajeError("Pago rechazado por el banco");
        assertEquals("Pago rechazado por el banco", dto.getMensajeError());
    }

    // ─── Todos los getters ──────────────────────────────────────────

    @Test
    public void testTodosLosGetters() {
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getReservaId());
        assertEquals(15000.0, dto.getMonto());
        assertEquals("TARJETA", dto.getMetodo());
        assertEquals("APROBADO", dto.getEstado());
        assertEquals("TXN-123456", dto.getCodigoTransaccion());
        assertEquals(fecha, dto.getFechaPago());
        assertNull(dto.getMensajeError());
    }

    // ─── toString() ─────────────────────────────────────────────────

    @Test
    public void testToString() {
        String toString = dto.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("reservaId=1"));
        assertTrue(toString.contains("monto=15000.0"));
        assertTrue(toString.contains("metodo=TARJETA"));
        assertTrue(toString.contains("estado=APROBADO"));
        assertTrue(toString.contains("codigoTransaccion=TXN-123456"));
        assertTrue(toString.contains("fechaPago=" + fecha));
    }

    @Test
    public void testToString_ConMensajeError() {
        dto.setMensajeError("Fondos insuficientes");
        String toString = dto.toString();
        assertTrue(toString.contains("mensajeError=Fondos insuficientes"));
    }

    // ─── equals() y hashCode() ─────────────────────────────────────

    @Test
    public void testEquals_CuandoSonIguales() {
        PagoResponseDTO dto1 = new PagoResponseDTO(
                1L, 1L, 15000.0, "TARJETA", "APROBADO", "TXN-123456", fecha, null
        );
        PagoResponseDTO dto2 = new PagoResponseDTO(
                1L, 1L, 15000.0, "TARJETA", "APROBADO", "TXN-123456", fecha, null
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorId() {
        PagoResponseDTO dto1 = new PagoResponseDTO(
                1L, 1L, 15000.0, "TARJETA", "APROBADO", "TXN-123456", fecha, null
        );
        PagoResponseDTO dto2 = new PagoResponseDTO(
                2L, 1L, 15000.0, "TARJETA", "APROBADO", "TXN-123456", fecha, null
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorReservaId() {
        PagoResponseDTO dto1 = new PagoResponseDTO(
                1L, 1L, 15000.0, "TARJETA", "APROBADO", "TXN-123456", fecha, null
        );
        PagoResponseDTO dto2 = new PagoResponseDTO(
                1L, 2L, 15000.0, "TARJETA", "APROBADO", "TXN-123456", fecha, null
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorEstado() {
        PagoResponseDTO dto1 = new PagoResponseDTO(
                1L, 1L, 15000.0, "TARJETA", "APROBADO", "TXN-123456", fecha, null
        );
        PagoResponseDTO dto2 = new PagoResponseDTO(
                1L, 1L, 15000.0, "TARJETA", "RECHAZADO", "TXN-123456", fecha, null
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorCodigoTransaccion() {
        PagoResponseDTO dto1 = new PagoResponseDTO(
                1L, 1L, 15000.0, "TARJETA", "APROBADO", "TXN-123456", fecha, null
        );
        PagoResponseDTO dto2 = new PagoResponseDTO(
                1L, 1L, 15000.0, "TARJETA", "APROBADO", "TXN-999999", fecha, null
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorMensajeError() {
        PagoResponseDTO dto1 = new PagoResponseDTO(
                1L, 1L, 15000.0, "TARJETA", "RECHAZADO", "TXN-123456", fecha, null
        );
        PagoResponseDTO dto2 = new PagoResponseDTO(
                1L, 1L, 15000.0, "TARJETA", "RECHAZADO", "TXN-123456", fecha, "Fondos insuficientes"
        );

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
    public void testMontoMuyGrande() {
        dto.setMonto(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, dto.getMonto());
    }

    @Test
    public void testIdMuyGrande() {
        dto.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, dto.getId());
    }

    @Test
    public void testReservaIdMuyGrande() {
        dto.setReservaId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, dto.getReservaId());
    }

    // ─── Tests de estados ───────────────────────────────────────────

    @Test
    public void testEstadoProcesando() {
        dto.setEstado("PROCESANDO");
        assertEquals("PROCESANDO", dto.getEstado());
    }

    @Test
    public void testEstadoAprobado() {
        dto.setEstado("APROBADO");
        assertEquals("APROBADO", dto.getEstado());
    }

    @Test
    public void testEstadoRechazado() {
        dto.setEstado("RECHAZADO");
        assertEquals("RECHAZADO", dto.getEstado());
    }

    @Test
    public void testEstadoTimeout() {
        dto.setEstado("TIMEOUT");
        assertEquals("TIMEOUT", dto.getEstado());
    }

    // ─── Tests de mensaje de error ─────────────────────────────────

    @Test
    public void testMensajeErrorConValor() {
        dto.setMensajeError("Fondos insuficientes");
        assertEquals("Fondos insuficientes", dto.getMensajeError());
    }

    @Test
    public void testMensajeErrorNull() {
        dto.setMensajeError(null);
        assertNull(dto.getMensajeError());
    }

    @Test
    public void testMensajeErrorVacio() {
        dto.setMensajeError("");
        assertEquals("", dto.getMensajeError());
    }

    // ─── Tests de código de transacción ─────────────────────────────

    @Test
    public void testCodigoTransaccionEmpiezaConTXN() {
        dto.setCodigoTransaccion("TXN-123456");
        assertTrue(dto.getCodigoTransaccion().startsWith("TXN-"));
    }

    @Test
    public void testCodigoTransaccionNull() {
        dto.setCodigoTransaccion(null);
        assertNull(dto.getCodigoTransaccion());
    }

    // ─── Tests de fecha ─────────────────────────────────────────────

    @Test
    public void testFechaPagoEsPasado() {
        LocalDateTime pasado = LocalDateTime.now().minusDays(1);
        dto.setFechaPago(pasado);
        assertTrue(dto.getFechaPago().isBefore(LocalDateTime.now()));
    }

    @Test
    public void testFechaPagoEsFuturo() {
        LocalDateTime futuro = LocalDateTime.now().plusDays(1);
        dto.setFechaPago(futuro);
        assertTrue(dto.getFechaPago().isAfter(LocalDateTime.now()));
    }

    // ─── Test de consistencia ───────────────────────────────────────

    @Test
    public void testNingunCampoEsNullDespuesDeSetear() {
        assertNotNull(dto.getId());
        assertNotNull(dto.getReservaId());
        assertNotNull(dto.getMonto());
        assertNotNull(dto.getMetodo());
        assertNotNull(dto.getEstado());
        assertNotNull(dto.getCodigoTransaccion());
        assertNotNull(dto.getFechaPago());
    }

    @Test
    public void testTodosLosCamposPuedenSerNull() {
        PagoResponseDTO dtoNull = new PagoResponseDTO();
        assertNull(dtoNull.getId());
        assertNull(dtoNull.getReservaId());
        assertNull(dtoNull.getMonto());
        assertNull(dtoNull.getMetodo());
        assertNull(dtoNull.getEstado());
        assertNull(dtoNull.getCodigoTransaccion());
        assertNull(dtoNull.getFechaPago());
        assertNull(dtoNull.getMensajeError());
    }
}