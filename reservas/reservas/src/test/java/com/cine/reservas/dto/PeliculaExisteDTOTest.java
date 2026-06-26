package com.cine.reservas.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PeliculaExisteDTOTest {

    private PeliculaExisteDTO dto;

    @BeforeEach
    void setUp() {
        dto = new PeliculaExisteDTO();
        dto.setExiste(true);
    }

    // ─── Constructor vacío ──────────────────────────────────────────

    @Test
    public void testConstructorVacio() {
        PeliculaExisteDTO dtoVacio = new PeliculaExisteDTO();
        assertNotNull(dtoVacio);
        assertFalse(dtoVacio.isExiste());
    }

    // ─── Constructor con parámetros ─────────────────────────────────

    @Test
    public void testConstructorConParametros() {
        PeliculaExisteDTO dtoConParams = new PeliculaExisteDTO(true);

        assertTrue(dtoConParams.isExiste());
    }

    @Test
    public void testConstructorConParametros_Falso() {
        PeliculaExisteDTO dtoConParams = new PeliculaExisteDTO(false);

        assertFalse(dtoConParams.isExiste());
    }

    // ─── Getters y Setters ──────────────────────────────────────────

    @Test
    public void testSetExiste() {
        dto.setExiste(false);
        assertFalse(dto.isExiste());
    }

    @Test
    public void testSetExiste_Verdadero() {
        dto.setExiste(true);
        assertTrue(dto.isExiste());
    }

    @Test
    public void testSetExiste_Falso() {
        dto.setExiste(false);
        assertFalse(dto.isExiste());
    }

    // ─── Getter ─────────────────────────────────────────────────────

    @Test
    public void testIsExiste_CuandoEsVerdadero() {
        dto.setExiste(true);
        assertTrue(dto.isExiste());
    }

    @Test
    public void testIsExiste_CuandoEsFalso() {
        dto.setExiste(false);
        assertFalse(dto.isExiste());
    }

    // ─── toString() ─────────────────────────────────────────────────

    @Test
    public void testToString_CuandoEsVerdadero() {
        dto.setExiste(true);
        String toString = dto.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("existe=true"));
    }

    @Test
    public void testToString_CuandoEsFalso() {
        dto.setExiste(false);
        String toString = dto.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("existe=false"));
    }

    // ─── equals() y hashCode() ─────────────────────────────────────

    @Test
    public void testEquals_CuandoSonIguales() {
        PeliculaExisteDTO dto1 = new PeliculaExisteDTO(true);
        PeliculaExisteDTO dto2 = new PeliculaExisteDTO(true);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEquals_CuandoSonIguales_Falso() {
        PeliculaExisteDTO dto1 = new PeliculaExisteDTO(false);
        PeliculaExisteDTO dto2 = new PeliculaExisteDTO(false);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEquals_CuandoSonDiferentes() {
        PeliculaExisteDTO dto1 = new PeliculaExisteDTO(true);
        PeliculaExisteDTO dto2 = new PeliculaExisteDTO(false);

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

    @Test
    public void testEquals_CuandoEsDiferenteClase() {
        assertNotEquals(dto, "un string");
    }

    // ─── Tests de valores ───────────────────────────────────────────

    @Test
    public void testValorPorDefectoEsFalso() {
        PeliculaExisteDTO dtoNuevo = new PeliculaExisteDTO();
        assertFalse(dtoNuevo.isExiste());
    }

    @Test
    public void testSetExisteConBooleanWrapper() {
        Boolean valor = true;
        dto.setExiste(valor);
        assertTrue(dto.isExiste());
    }

    @Test
    public void testSetExisteConBooleanWrapper_Falso() {
        Boolean valor = false;
        dto.setExiste(valor);
        assertFalse(dto.isExiste());
    }

    // ─── Test de consistencia ───────────────────────────────────────

    @Test
    public void testNingunCampoEsNullDespuesDeSetear() {
        assertNotNull(dto.isExiste());
    }

    @Test
    public void testTodosLosCamposPuedenSerNull() {
        PeliculaExisteDTO dtoNull = new PeliculaExisteDTO();
        assertFalse(dtoNull.isExiste());
    }
}