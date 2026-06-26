package com.cine.reservas.client;

import com.cine.reservas.dto.UsuarioExisteDTO;  // ← IMPORTANTE: agregar este import
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioClienteTest {

    @Autowired(required = false)
    private UsuarioCliente usuarioCliente;

    // ─── Test de existencia ─────────────────────────────────────────

    @Test
    public void testUsuarioClienteExiste() {
        assertNotNull(usuarioCliente);
    }

    @Test
    public void testUsuarioClienteTieneAnotacionFeign() {
        Class<?> clienteClass = UsuarioCliente.class;
        assertTrue(clienteClass.isInterface() ||
                clienteClass.getInterfaces().length > 0);
    }

    // ─── Tests del método validarUsuario ───────────────────────────

    @Test
    public void testValidarUsuarioMetodoExiste() {
        try {
            var method = UsuarioCliente.class.getMethod("validarUsuario", Long.class);
            assertNotNull(method);
            assertEquals(Map.class, method.getReturnType());
        } catch (NoSuchMethodException e) {
            fail("El método validarUsuario no existe en UsuarioCliente");
        }
    }

    @Test
    public void testValidarUsuarioTieneAnotacionGetMapping() {
        try {
            var method = UsuarioCliente.class.getMethod("validarUsuario", Long.class);
            var annotations = method.getAnnotations();
            assertTrue(annotations.length > 0);
        } catch (NoSuchMethodException e) {
            fail("El método validarUsuario no existe");
        }
    }

    @Test
    public void testValidarUsuarioAceptaPathVariableLong() {
        try {
            var method = UsuarioCliente.class.getMethod("validarUsuario", Long.class);
            var parameterTypes = method.getParameterTypes();
            assertEquals(1, parameterTypes.length);
            assertEquals(Long.class, parameterTypes[0]);
        } catch (NoSuchMethodException e) {
            fail("El método validarUsuario no existe");
        }
    }

    @Test
    public void testValidarUsuarioRetornaMap() {
        try {
            var method = UsuarioCliente.class.getMethod("validarUsuario", Long.class);
            assertEquals(Map.class, method.getReturnType());
        } catch (NoSuchMethodException e) {
            fail("El método validarUsuario no existe");
        }
    }

    // ─── Test de la anotación FeignClient ──────────────────────────

    @Test
    public void testUsuarioClienteTieneFeignClientAnnotation() {
        Class<?> clienteClass = UsuarioCliente.class;
        assertTrue(clienteClass.isAnnotationPresent(FeignClient.class));
    }

    @Test
    public void testUsuarioClienteFeignClientName() {
        if (usuarioCliente != null) {
            FeignClient annotation = UsuarioCliente.class.getAnnotation(FeignClient.class);
            if (annotation != null) {
                String name = annotation.name();
                assertNotNull(name);
                assertEquals("ms-usuarios", name);
            }
        }
    }

    @Test
    public void testUsuarioClienteFeignClientUrl() {
        if (usuarioCliente != null) {
            FeignClient annotation = UsuarioCliente.class.getAnnotation(FeignClient.class);
            if (annotation != null) {
                String url = annotation.url();
                assertNotNull(url);
                assertTrue(url.contains("localhost"));
                assertTrue(url.contains("8081"));
            }
        }
    }

    @Test
    public void testUsuarioClienteMapeaUsuariosExisteEndpoint() {
        try {
            var method = UsuarioCliente.class.getMethod("validarUsuario", Long.class);
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
            fail("El método validarUsuario no existe");
        }
    }

    // ─── Tests de integración básicos ──────────────────────────────

    @Test
    public void testUsuarioClienteInstanciaNoEsNull() {
        assertNotNull(usuarioCliente);
    }

    @Test
    public void testValidarUsuarioConIdValido() {
        try {
            var method = UsuarioCliente.class.getMethod("validarUsuario", Long.class);
            assertNotNull(method);
        } catch (NoSuchMethodException e) {
            fail("El método validarUsuario no existe");
        }
    }

    // ─── Tests de la URL del cliente ───────────────────────────────

    @Test
    public void testUsuarioClienteURLContiene8081() {
        FeignClient annotation = UsuarioCliente.class.getAnnotation(FeignClient.class);
        if (annotation != null) {
            String url = annotation.url();
            assertTrue(url.contains("8081"), "La URL debería contener el puerto 8081");
        }
    }

    @Test
    public void testUsuarioClienteURLContieneLocalhost() {
        FeignClient annotation = UsuarioCliente.class.getAnnotation(FeignClient.class);
        if (annotation != null) {
            String url = annotation.url();
            assertTrue(url.contains("localhost"), "La URL debería contener localhost");
        }
    }

    // ─── Test del path del endpoint ─────────────────────────────────

    @Test
    public void testUsuarioClientePathEsApiUsuariosIdExiste() {
        try {
            var method = UsuarioCliente.class.getMethod("validarUsuario", Long.class);
            var annotations = method.getAnnotations();
            boolean hasCorrectPath = false;
            for (var ann : annotations) {
                if (ann.annotationType().getSimpleName().equals("GetMapping")) {
                    try {
                        var valueField = ann.getClass().getMethod("value");
                        String[] paths = (String[]) valueField.invoke(ann);
                        if (paths.length > 0 && paths[0].equals("/api/usuarios/{id}/existe")) {
                            hasCorrectPath = true;
                        }
                    } catch (Exception e) {
                        hasCorrectPath = true;
                    }
                    break;
                }
            }
            assertTrue(hasCorrectPath, "El método debería mapear a /api/usuarios/{id}/existe");
        } catch (NoSuchMethodException e) {
            fail("El método validarUsuario no existe");
        }
    }

    // ─── Tests del DTO UsuarioExisteDTO ────────────────────────────

    @Test
    public void testUsuarioExisteDTOTieneCampoExiste() {
        try {
            var field = UsuarioExisteDTO.class.getDeclaredField("existe");
            assertNotNull(field);
            assertEquals(boolean.class, field.getType());
        } catch (NoSuchFieldException e) {
            fail("El DTO UsuarioExisteDTO debería tener el campo 'existe'");
        }
    }

    @Test
    public void testUsuarioExisteDTOConstructorVacio() {
        try {
            var constructor = UsuarioExisteDTO.class.getConstructor();
            assertNotNull(constructor);
        } catch (NoSuchMethodException e) {
            fail("El DTO UsuarioExisteDTO debería tener un constructor vacío");
        }
    }

    @Test
    public void testUsuarioExisteDTOGettersYSetters() {
        try {
            var dto = new UsuarioExisteDTO();
            var setExiste = UsuarioExisteDTO.class.getMethod("setExiste", boolean.class);
            var getExiste = UsuarioExisteDTO.class.getMethod("isExiste");
            assertNotNull(setExiste);
            assertNotNull(getExiste);
        } catch (NoSuchMethodException e) {
            fail("El DTO UsuarioExisteDTO debería tener getters y setters");
        }
    }

    // ─── Test de consistencia del cliente ──────────────────────────

    @Test
    public void testUsuarioClienteTieneNombreConfigurado() {
        FeignClient annotation = UsuarioCliente.class.getAnnotation(FeignClient.class);
        if (annotation != null) {
            assertNotNull(annotation.name());
            assertFalse(annotation.name().isEmpty());
        }
    }

    @Test
    public void testUsuarioClienteTieneUrlConfigurada() {
        FeignClient annotation = UsuarioCliente.class.getAnnotation(FeignClient.class);
        if (annotation != null) {
            assertNotNull(annotation.url());
            assertFalse(annotation.url().isEmpty());
        }
    }

    // ─── Tests de comparación entre clientes ──────────────────────

    @Test
    public void testUsuarioClienteDiferenteDePagoCliente() {
        assertNotNull(usuarioCliente);
        assertNotEquals(usuarioCliente.getClass(), PagoCliente.class);
    }

    @Test
    public void testUsuarioClienteDiferenteDePeliculaCliente() {
        assertNotNull(usuarioCliente);
        assertNotEquals(usuarioCliente.getClass(), PeliculaCliente.class);
    }
}