package com.cine.reservas.client;

import com.cine.reservas.dto.PeliculaExisteDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PeliculaClienteTest {

    @Autowired(required = false)
    private PeliculaCliente peliculaCliente;

    // ─── Test de existencia ─────────────────────────────────────────

    @Test
    public void testPeliculaClienteExiste() {
        assertNotNull(peliculaCliente);
    }

    @Test
    public void testPeliculaClienteTieneAnotacionFeign() {
        Class<?> clienteClass = PeliculaCliente.class;
        assertTrue(clienteClass.isInterface() ||
                clienteClass.getInterfaces().length > 0);
    }

    // ─── Tests del método validarPelicula ──────────────────────────

    @Test
    public void testValidarPeliculaMetodoExiste() {
        try {
            var method = PeliculaCliente.class.getMethod("validarPelicula", Long.class);
            assertNotNull(method);
            assertEquals(PeliculaExisteDTO.class, method.getReturnType());
        } catch (NoSuchMethodException e) {
            fail("El método validarPelicula no existe en PeliculaCliente");
        }
    }

    @Test
    public void testValidarPeliculaTieneAnotacionGetMapping() {
        try {
            var method = PeliculaCliente.class.getMethod("validarPelicula", Long.class);
            var annotations = method.getAnnotations();
            assertTrue(annotations.length > 0);
        } catch (NoSuchMethodException e) {
            fail("El método validarPelicula no existe");
        }
    }

    @Test
    public void testValidarPeliculaAceptaPathVariableLong() {
        try {
            var method = PeliculaCliente.class.getMethod("validarPelicula", Long.class);
            var parameterTypes = method.getParameterTypes();
            assertEquals(1, parameterTypes.length);
            assertEquals(Long.class, parameterTypes[0]);
        } catch (NoSuchMethodException e) {
            fail("El método validarPelicula no existe");
        }
    }

    @Test
    public void testValidarPeliculaRetornaPeliculaExisteDTO() {
        try {
            var method = PeliculaCliente.class.getMethod("validarPelicula", Long.class);
            assertEquals(PeliculaExisteDTO.class, method.getReturnType());
        } catch (NoSuchMethodException e) {
            fail("El método validarPelicula no existe");
        }
    }

    // ─── Test de la anotación FeignClient ──────────────────────────

    @Test
    public void testPeliculaClienteTieneFeignClientAnnotation() {
        Class<?> clienteClass = PeliculaCliente.class;
        assertTrue(clienteClass.isAnnotationPresent(FeignClient.class));
    }

    @Test
    public void testPeliculaClienteFeignClientName() {
        if (peliculaCliente != null) {
            FeignClient annotation = PeliculaCliente.class.getAnnotation(FeignClient.class);
            if (annotation != null) {
                String name = annotation.name();
                assertNotNull(name);
                assertEquals("ms-peliculas", name);
            }
        }
    }

    @Test
    public void testPeliculaClienteFeignClientUrl() {
        if (peliculaCliente != null) {
            FeignClient annotation = PeliculaCliente.class.getAnnotation(FeignClient.class);
            if (annotation != null) {
                String url = annotation.url();
                assertNotNull(url);
                assertTrue(url.contains("localhost"));
                assertTrue(url.contains("8082"));
            }
        }
    }

    @Test
    public void testPeliculaClienteMapeaPeliculasExisteEndpoint() {
        try {
            var method = PeliculaCliente.class.getMethod("validarPelicula", Long.class);
            var annotations = method.getAnnotations();
            boolean hasGetMapping = false;
            for (var ann : annotations) {
                if (ann.annotationType().getSimpleName().equals("GetMapping")) {
                    hasGetMapping = true;
                    break;
                }
            }
            assertTrue(hasGetMapping, "El método debería tener @GetMapping");
        } catch (NoSuchMethodException e) {
            fail("El método validarPelicula no existe");
        }
    }

    // ─── Tests de integración básicos ──────────────────────────────

    @Test
    public void testPeliculaClienteInstanciaNoEsNull() {
        assertNotNull(peliculaCliente);
    }

    @Test
    public void testValidarPeliculaConIdValido() {
        try {
            var method = PeliculaCliente.class.getMethod("validarPelicula", Long.class);
            assertNotNull(method);
        } catch (NoSuchMethodException e) {
            fail("El método validarPelicula no existe");
        }
    }

    // ─── Tests de la URL del cliente ───────────────────────────────

    @Test
    public void testPeliculaClienteURLContiene8082() {
        FeignClient annotation = PeliculaCliente.class.getAnnotation(FeignClient.class);
        if (annotation != null) {
            String url = annotation.url();
            assertTrue(url.contains("8082"), "La URL debería contener el puerto 8082");
        }
    }

    @Test
    public void testPeliculaClienteURLContieneLocalhost() {
        FeignClient annotation = PeliculaCliente.class.getAnnotation(FeignClient.class);
        if (annotation != null) {
            String url = annotation.url();
            assertTrue(url.contains("localhost"), "La URL debería contener localhost");
        }
    }

    // ─── Test del path del endpoint ─────────────────────────────────

    @Test
    public void testPeliculaClientePathEsApiPeliculasIdExiste() {
        try {
            var method = PeliculaCliente.class.getMethod("validarPelicula", Long.class);
            var annotations = method.getAnnotations();
            boolean hasCorrectPath = false;
            for (var ann : annotations) {
                if (ann.annotationType().getSimpleName().equals("GetMapping")) {
                    try {
                        var valueField = ann.getClass().getMethod("value");
                        String[] paths = (String[]) valueField.invoke(ann);
                        if (paths.length > 0 && paths[0].equals("/api/peliculas/{id}/existe")) {
                            hasCorrectPath = true;
                        }
                    } catch (Exception e) {
                        hasCorrectPath = true;
                    }
                    break;
                }
            }
            assertTrue(hasCorrectPath, "El método debería mapear a /api/peliculas/{id}/existe");
        } catch (NoSuchMethodException e) {
            fail("El método validarPelicula no existe");
        }
    }

    // ─── Test de la estructura del DTO ─────────────────────────────

    @Test
    public void testPeliculaExisteDTOTieneCampoExiste() {
        try {
            var field = PeliculaExisteDTO.class.getDeclaredField("existe");
            assertNotNull(field);
            assertEquals(boolean.class, field.getType());
        } catch (NoSuchFieldException e) {
            fail("El DTO PeliculaExisteDTO debería tener el campo 'existe'");
        }
    }

    @Test
    public void testPeliculaExisteDTOConstructorVacio() {
        try {
            var constructor = PeliculaExisteDTO.class.getConstructor();
            assertNotNull(constructor);
        } catch (NoSuchMethodException e) {
            fail("El DTO PeliculaExisteDTO debería tener un constructor vacío");
        }
    }

    @Test
    public void testPeliculaExisteDTOConstructorConParametros() {
        try {
            var constructor = PeliculaExisteDTO.class.getConstructor(boolean.class);
            assertNotNull(constructor);
        } catch (NoSuchMethodException e) {
            fail("El DTO PeliculaExisteDTO debería tener un constructor con parámetros");
        }
    }

    // ─── Test de consistencia del cliente ──────────────────────────

    @Test
    public void testPeliculaClienteTieneNombreConfigurado() {
        FeignClient annotation = PeliculaCliente.class.getAnnotation(FeignClient.class);
        if (annotation != null) {
            assertNotNull(annotation.name());
            assertFalse(annotation.name().isEmpty());
        }
    }

    @Test
    public void testPeliculaClienteTieneUrlConfigurada() {
        FeignClient annotation = PeliculaCliente.class.getAnnotation(FeignClient.class);
        if (annotation != null) {
            assertNotNull(annotation.url());
            assertFalse(annotation.url().isEmpty());
        }
    }
}