package com.cine.pagos.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PagoRequestDTOTest {

    private Validator validator;
    private PagoRequestDTO dto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        dto = new PagoRequestDTO();
        dto.setReservaId(1L);
        dto.setMonto(15000.0);
        dto.setMetodo("TARJETA");
    }

    // ─── Constructor vacío ──────────────────────────────────────────

    @Test
    public void testConstructorVacio() {
        PagoRequestDTO dtoVacio = new PagoRequestDTO();
        assertNotNull(dtoVacio);
        assertNull(dtoVacio.getReservaId());
        assertNull(dtoVacio.getMonto());
        assertNull(dtoVacio.getMetodo());
    }

    // ─── Constructor con parámetros ─────────────────────────────────

    @Test
    public void testConstructorConParametros() {
        PagoRequestDTO dtoConParams = new PagoRequestDTO(1L, 15000.0, "TARJETA");

        assertEquals(1L, dtoConParams.getReservaId());
        assertEquals(15000.0, dtoConParams.getMonto());
        assertEquals("TARJETA", dtoConParams.getMetodo());
    }

    // ─── Getters y Setters ──────────────────────────────────────────

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

    // ─── Validaciones: @NotNull ─────────────────────────────────────

    @Test
    public void testReservaIdNoPuedeSerNull() {
        dto.setReservaId(null);
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El ID de reserva es obligatorio")));
    }

    @Test
    public void testMontoNoPuedeSerNull() {
        dto.setMonto(null);
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El monto es obligatorio")));
    }

    // ─── Validaciones: @Positive ────────────────────────────────────

    @Test
    public void testMontoDebeSerPositivo() {
        dto.setMonto(-15000.0);
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El monto debe ser positivo")));
    }

    @Test
    public void testMontoCero_NoEsValido() {
        dto.setMonto(0.0);
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El monto debe ser positivo")));
    }

    // ─── Validaciones: @NotBlank ────────────────────────────────────

    @Test
    public void testMetodoNoPuedeSerVacio() {
        dto.setMetodo("");
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El método de pago es obligatorio")));
    }

    @Test
    public void testMetodoNoPuedeSerNull() {
        dto.setMetodo(null);
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El método de pago es obligatorio")));
    }

    @Test
    public void testMetodoNoPuedeSerSoloEspacios() {
        dto.setMetodo("   ");
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El método de pago es obligatorio")));
    }

    // ─── Validaciones: DTO válido ──────────────────────────────────

    @Test
    public void testDTOValido() {
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // ─── toString() ─────────────────────────────────────────────────

    @Test
    public void testToString() {
        String toString = dto.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("reservaId=1"));
        assertTrue(toString.contains("monto=15000.0"));
        assertTrue(toString.contains("metodo=TARJETA"));
    }

    // ─── equals() y hashCode() ─────────────────────────────────────

    @Test
    public void testEquals_CuandoSonIguales() {
        PagoRequestDTO dto1 = new PagoRequestDTO(1L, 15000.0, "TARJETA");
        PagoRequestDTO dto2 = new PagoRequestDTO(1L, 15000.0, "TARJETA");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorReservaId() {
        PagoRequestDTO dto1 = new PagoRequestDTO(1L, 15000.0, "TARJETA");
        PagoRequestDTO dto2 = new PagoRequestDTO(2L, 15000.0, "TARJETA");
        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorMonto() {
        PagoRequestDTO dto1 = new PagoRequestDTO(1L, 15000.0, "TARJETA");
        PagoRequestDTO dto2 = new PagoRequestDTO(1L, 20000.0, "TARJETA");
        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorMetodo() {
        PagoRequestDTO dto1 = new PagoRequestDTO(1L, 15000.0, "TARJETA");
        PagoRequestDTO dto2 = new PagoRequestDTO(1L, 15000.0, "EFECTIVO");
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
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testReservaIdMuyGrande() {
        dto.setReservaId(Long.MAX_VALUE);
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testMetodoConEspacios() {
        dto.setMetodo("  TARJETA  ");
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // ─── Tests de métodos de pago válidos ──────────────────────────

    @Test
    public void testMetodoTarjetaValido() {
        dto.setMetodo("TARJETA");
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        assertEquals("TARJETA", dto.getMetodo());
    }

    @Test
    public void testMetodoEfectivoValido() {
        dto.setMetodo("EFECTIVO");
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        assertEquals("EFECTIVO", dto.getMetodo());
    }

    @Test
    public void testMetodoTransferenciaValido() {
        dto.setMetodo("TRANSFERENCIA");
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        assertEquals("TRANSFERENCIA", dto.getMetodo());
    }

    @Test
    public void testMetodoEnMinusculas() {
        dto.setMetodo("tarjeta");
        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        assertEquals("tarjeta", dto.getMetodo());
    }

    // ─── Tests de todos los getters juntos ─────────────────────────

    @Test
    public void testTodosLosGetters() {
        assertEquals(1L, dto.getReservaId());
        assertEquals(15000.0, dto.getMonto());
        assertEquals("TARJETA", dto.getMetodo());
    }

    // ─── Test de múltiples validaciones fallando ──────────────────

    @Test
    public void testMultiplesValidacionesFallando() {
        PagoRequestDTO dtoInvalido = new PagoRequestDTO();
        dtoInvalido.setMonto(-15000.0);
        dtoInvalido.setMetodo("");

        Set<ConstraintViolation<PagoRequestDTO>> violations = validator.validate(dtoInvalido);
        assertTrue(violations.size() >= 3);
    }

    // ─── Test de consistencia ───────────────────────────────────────

    @Test
    public void testNingunCampoEsNullDespuesDeSetear() {
        assertNotNull(dto.getReservaId());
        assertNotNull(dto.getMonto());
        assertNotNull(dto.getMetodo());
    }

    @Test
    public void testTodosLosCamposPuedenSerNull() {
        PagoRequestDTO dtoNull = new PagoRequestDTO();
        assertNull(dtoNull.getReservaId());
        assertNull(dtoNull.getMonto());
        assertNull(dtoNull.getMetodo());
    }
}