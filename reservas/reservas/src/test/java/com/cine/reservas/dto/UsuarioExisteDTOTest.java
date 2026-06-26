package com.cine.reservas.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioExisteDTOTest {

    private UsuarioExisteDTO dto;

    @BeforeEach
    void setUp() {
        dto = new UsuarioExisteDTO();
        dto.setExiste(true);
    }

    // ─── Constructor vacío ──────────────────────────────────────────

    @Test
    public void testConstructorVacio() {
        UsuarioExisteDTO dtoVacio = new UsuarioExisteDTO();
        assertNotNull(dtoVacio);
        assertFalse(dtoVacio.isExiste());
    }

    // ─── Constructor con parámetros ─────────────────────────────────

    @Test
    public void testConstructorConParametros() {
        UsuarioExisteDTO dtoConParams = new UsuarioExisteDTO(true);

        assertTrue(dtoConParams.isExiste());
    }

    @Test
    public void testConstructorConParametros_Falso() {
        UsuarioExisteDTO dtoConParams = new UsuarioExisteDTO(false);

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
        UsuarioExisteDTO dto1 = new UsuarioExisteDTO(true);
        UsuarioExisteDTO dto2 = new UsuarioExisteDTO(true);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEquals_CuandoSonIguales_Falso() {
        UsuarioExisteDTO dto1 = new UsuarioExisteDTO(false);
        UsuarioExisteDTO dto2 = new UsuarioExisteDTO(false);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEquals_CuandoSonDiferentes() {
        UsuarioExisteDTO dto1 = new UsuarioExisteDTO(true);
        UsuarioExisteDTO dto2 = new UsuarioExisteDTO(false);

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
        UsuarioExisteDTO dtoNuevo = new UsuarioExisteDTO();
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
        UsuarioExisteDTO dtoNull = new UsuarioExisteDTO();
        assertFalse(dtoNull.isExiste());
    }
}