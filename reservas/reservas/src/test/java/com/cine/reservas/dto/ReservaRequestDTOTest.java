package com.cine.reservas.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaRequestDTOTest {

    private Validator validator;
    private ReservaRequestDTO dto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        dto = new ReservaRequestDTO();
        dto.setUsuario_id(1L);
        dto.setFuncion_id(1L);
        dto.setCantidad_de_asientos(2);
        dto.setTotal(15000.0);
    }

    // ─── Constructor vacío ──────────────────────────────────────────

    @Test
    public void testConstructorVacio() {
        ReservaRequestDTO dtoVacio = new ReservaRequestDTO();
        assertNotNull(dtoVacio);
        assertNull(dtoVacio.getUsuario_id());
        assertNull(dtoVacio.getFuncion_id());
        assertNull(dtoVacio.getCantidad_de_asientos());
        assertNull(dtoVacio.getTotal());
    }

    // ─── Constructor con parámetros ─────────────────────────────────

    @Test
    public void testConstructorConParametros() {
        ReservaRequestDTO dtoConParams = new ReservaRequestDTO(1L, 1L, 2, 15000.0);

        assertEquals(1L, dtoConParams.getUsuario_id());
        assertEquals(1L, dtoConParams.getFuncion_id());
        assertEquals(2, dtoConParams.getCantidad_de_asientos());
        assertEquals(15000.0, dtoConParams.getTotal());
    }

    // ─── Getters y Setters ──────────────────────────────────────────

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

    // ─── Validaciones: @NotNull ─────────────────────────────────────

    @Test
    public void testUsuario_idNoPuedeSerNull() {
        dto.setUsuario_id(null);

        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El usuario_id es obligatorio")));
    }

    @Test
    public void testFuncion_idNoPuedeSerNull() {
        dto.setFuncion_id(null);

        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El funcion_id es obligatorio")));
    }

    @Test
    public void testCantidad_de_asientosNoPuedeSerNull() {
        dto.setCantidad_de_asientos(null);

        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("La cantidad de asientos es obligatoria")));
    }

    @Test
    public void testTotalNoPuedeSerNull() {
        dto.setTotal(null);

        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El total es obligatorio")));
    }

    // ─── Validaciones: @Positive ────────────────────────────────────

    @Test
    public void testCantidad_de_asientosDebeSerPositivo() {
        dto.setCantidad_de_asientos(-1);

        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("La cantidad de asientos debe ser mayor a 0")));
    }

    @Test
    public void testCantidad_de_asientosCero_NoEsValido() {
        dto.setCantidad_de_asientos(0);

        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("La cantidad de asientos debe ser mayor a 0")));
    }

    @Test
    public void testTotalDebeSerPositivo() {
        dto.setTotal(-15000.0);

        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El total debe ser mayor a 0")));
    }

    @Test
    public void testTotalCero_NoEsValido() {
        dto.setTotal(0.0);

        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El total debe ser mayor a 0")));
    }

    // ─── Validaciones: DTO válido ──────────────────────────────────

    @Test
    public void testDTOValido() {
        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    // ─── toString() ─────────────────────────────────────────────────

    @Test
    public void testToString() {
        String toString = dto.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("usuario_id=1"));
        assertTrue(toString.contains("funcion_id=1"));
        assertTrue(toString.contains("cantidad_de_asientos=2"));
        assertTrue(toString.contains("total=15000.0"));
    }

    // ─── equals() y hashCode() ─────────────────────────────────────

    @Test
    public void testEquals_CuandoSonIguales() {
        ReservaRequestDTO dto1 = new ReservaRequestDTO(1L, 1L, 2, 15000.0);
        ReservaRequestDTO dto2 = new ReservaRequestDTO(1L, 1L, 2, 15000.0);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorUsuarioId() {
        ReservaRequestDTO dto1 = new ReservaRequestDTO(1L, 1L, 2, 15000.0);
        ReservaRequestDTO dto2 = new ReservaRequestDTO(2L, 1L, 2, 15000.0);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorFuncionId() {
        ReservaRequestDTO dto1 = new ReservaRequestDTO(1L, 1L, 2, 15000.0);
        ReservaRequestDTO dto2 = new ReservaRequestDTO(1L, 2L, 2, 15000.0);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorCantidad() {
        ReservaRequestDTO dto1 = new ReservaRequestDTO(1L, 1L, 2, 15000.0);
        ReservaRequestDTO dto2 = new ReservaRequestDTO(1L, 1L, 3, 15000.0);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testEquals_CuandoSonDiferentesPorTotal() {
        ReservaRequestDTO dto1 = new ReservaRequestDTO(1L, 1L, 2, 15000.0);
        ReservaRequestDTO dto2 = new ReservaRequestDTO(1L, 1L, 2, 20000.0);

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
        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testTotalMuyGrande() {
        dto.setTotal(Double.MAX_VALUE);
        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testUsuarioIdMuyGrande() {
        dto.setUsuario_id(Long.MAX_VALUE);
        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testFuncionIdMuyGrande() {
        dto.setFuncion_id(Long.MAX_VALUE);
        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // ─── Tests de todos los getters juntos ─────────────────────────

    @Test
    public void testTodosLosGetters() {
        assertEquals(1L, dto.getUsuario_id());
        assertEquals(1L, dto.getFuncion_id());
        assertEquals(2, dto.getCantidad_de_asientos());
        assertEquals(15000.0, dto.getTotal());
    }

    // ─── Test de múltiples validaciones fallando ──────────────────

    @Test
    public void testMultiplesValidacionesFallando() {
        ReservaRequestDTO dtoInvalido = new ReservaRequestDTO();
        dtoInvalido.setCantidad_de_asientos(-1);
        dtoInvalido.setTotal(-15000.0);

        Set<ConstraintViolation<ReservaRequestDTO>> violations = validator.validate(dtoInvalido);

        // Debería haber al menos 4 violaciones (2 @NotNull + 2 @Positive)
        assertTrue(violations.size() >= 4);
    }
}