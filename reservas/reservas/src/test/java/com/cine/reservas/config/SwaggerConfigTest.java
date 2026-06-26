package com.cine.reservas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class SwaggerConfigTest {

    @Autowired(required = false)
    private SwaggerConfig swaggerConfig;

    private OpenAPI openAPI;

    @BeforeEach
    void setUp() {
        if (swaggerConfig != null) {
            openAPI = swaggerConfig.customOpenAPI();
        }
    }

    // ─── Test de existencia ─────────────────────────────────────────

    @Test
    public void testSwaggerConfigExists() {
        assertNotNull(swaggerConfig);
    }

    @Test
    public void testCustomOpenAPINotNull() {
        assertNotNull(openAPI);
    }

    // ─── Tests de la información de la API ─────────────────────────

    @Test
    public void testInfoNoEsNull() {
        Info info = openAPI.getInfo();
        assertNotNull(info);
    }

    @Test
    public void testInfoTieneTitle() {
        Info info = openAPI.getInfo();
        String title = info.getTitle();
        assertNotNull(title);
        assertFalse(title.isEmpty());
        assertEquals("API Reservas - Sistema de Cine", title);
    }

    @Test
    public void testInfoTieneVersion() {
        Info info = openAPI.getInfo();
        String version = info.getVersion();
        assertNotNull(version);
        assertFalse(version.isEmpty());
        assertEquals("1.0", version);
    }

    @Test
    public void testInfoTieneDescription() {
        Info info = openAPI.getInfo();
        String description = info.getDescription();
        assertNotNull(description);
        assertFalse(description.isEmpty());
        assertEquals("Documentación de la API para el sistema de reservas de cine", description);
    }

    // ─── Tests de los campos de Info ───────────────────────────────

    @Test
    public void testInfoTitleContienePalabraReservas() {
        Info info = openAPI.getInfo();
        String title = info.getTitle();
        assertTrue(title.contains("Reservas"));
    }

    @Test
    public void testInfoTitleContienePalabraAPI() {
        Info info = openAPI.getInfo();
        String title = info.getTitle();
        assertTrue(title.contains("API"));
    }

    @Test
    public void testInfoDescriptionContienePalabraDocumentacion() {
        Info info = openAPI.getInfo();
        String description = info.getDescription();
        assertTrue(description.contains("Documentación"));
    }

    @Test
    public void testInfoDescriptionContienePalabraReservas() {
        Info info = openAPI.getInfo();
        String description = info.getDescription();
        assertTrue(description.contains("reservas"));
    }

    @Test
    public void testInfoVersionEmpiezaConUno() {
        Info info = openAPI.getInfo();
        String version = info.getVersion();
        assertTrue(version.startsWith("1"));
    }

    // ─── Tests de consistencia ──────────────────────────────────────

    @Test
    public void testInfoTodosLosCamposEstanCompletos() {
        Info info = openAPI.getInfo();
        assertNotNull(info.getTitle());
        assertNotNull(info.getVersion());
        assertNotNull(info.getDescription());
    }

    @Test
    public void testInfoNoTieneCamposVacios() {
        Info info = openAPI.getInfo();
        assertFalse(info.getTitle().trim().isEmpty());
        assertFalse(info.getVersion().trim().isEmpty());
        assertFalse(info.getDescription().trim().isEmpty());
    }

    @Test
    public void testOpenAPINoEsNull() {
        assertNotNull(openAPI);
    }

    @Test
    public void testOpenAPIInfoNoEsNull() {
        assertNotNull(openAPI.getInfo());
    }

    // ─── Test de formato de la versión ─────────────────────────────

    @Test
    public void testInfoVersionFormatoMayorMenor() {
        Info info = openAPI.getInfo();
        String version = info.getVersion();
        assertTrue(version.matches("^\\d+\\.\\d+$"),
                "La versión debería tener formato 'X.Y' (ejemplo: 1.0)");
    }

    // ─── Test de contenido del título ──────────────────────────────

    @Test
    public void testInfoTitleNoContieneEspaciosExtras() {
        Info info = openAPI.getInfo();
        String title = info.getTitle();
        assertEquals(title.trim(), title);
    }

    @Test
    public void testInfoDescriptionNoContieneEspaciosExtras() {
        Info info = openAPI.getInfo();
        String description = info.getDescription();
        assertEquals(description.trim(), description);
    }

    // ─── Test de longitud de campos ────────────────────────────────

    @Test
    public void testInfoTitleNoEsDemasiadoLargo() {
        Info info = openAPI.getInfo();
        String title = info.getTitle();
        assertTrue(title.length() < 100,
                "El título debería tener menos de 100 caracteres");
    }

    @Test
    public void testInfoDescriptionNoEsDemasiadoLarga() {
        Info info = openAPI.getInfo();
        String description = info.getDescription();
        assertTrue(description.length() < 200,
                "La descripción debería tener menos de 200 caracteres");
    }
}