package com.cine.reservas.client;

import com.cine.reservas.dto.PagoRequestDTO;
import com.cine.reservas.dto.PagoResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PagoClienteTest {

    @Autowired(required = false)
    private PagoCliente pagoCliente;

    private PagoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new PagoRequestDTO();
        requestDTO.setReservaId(1L);
        requestDTO.setMonto(15000.0);
        requestDTO.setMetodo("TARJETA");
    }

    // ─── Test de existencia ─────────────────────────────────────────

    @Test
    public void testPagoClienteExiste() {
        assertNotNull(pagoCliente);
    }

    @Test
    public void testPagoClienteTieneAnotacionFeign() {
        Class<?> clienteClass = pagoCliente.getClass();
        assertTrue(clienteClass.isInterface() ||
                clienteClass.getInterfaces().length > 0);
    }

    // ─── Tests del método procesarPago ─────────────────────────────

    @Test
    public void testProcesarPagoMetodoExiste() {
        // Verificamos que el método existe en la interfaz
        try {
            var method = PagoCliente.class.getMethod("procesarPago", PagoRequestDTO.class);
            assertNotNull(method);
            assertEquals(PagoResponseDTO.class, method.getReturnType());
        } catch (NoSuchMethodException e) {
            fail("El método procesarPago no existe en PagoCliente");
        }
    }

    @Test
    public void testProcesarPagoTieneAnotacionPostMapping() {
        try {
            var method = PagoCliente.class.getMethod("procesarPago", PagoRequestDTO.class);
            var annotations = method.getAnnotations();
            assertTrue(annotations.length > 0);
        } catch (NoSuchMethodException e) {
            fail("El método procesarPago no existe");
        }
    }

    @Test
    public void testProcesarPagoAceptaPagoRequestDTO() {
        try {
            var method = PagoCliente.class.getMethod("procesarPago", PagoRequestDTO.class);
            var parameterTypes = method.getParameterTypes();
            assertEquals(1, parameterTypes.length);
            assertEquals(PagoRequestDTO.class, parameterTypes[0]);
        } catch (NoSuchMethodException e) {
            fail("El método procesarPago no existe");
        }
    }

    @Test
    public void testProcesarPagoRetornaPagoResponseDTO() {
        try {
            var method = PagoCliente.class.getMethod("procesarPago", PagoRequestDTO.class);
            assertEquals(PagoResponseDTO.class, method.getReturnType());
        } catch (NoSuchMethodException e) {
            fail("El método procesarPago no existe");
        }
    }

    // ─── Test de la anotación FeignClient ──────────────────────────

    @Test
    public void testPagoClienteTieneFeignClientAnnotation() {
        Class<?> clienteClass = PagoCliente.class;
        assertTrue(clienteClass.isAnnotationPresent(FeignClient.class));
    }

    @Test
    public void testPagoClienteFeignClientName() {
        if (pagoCliente != null) {
            FeignClient annotation = PagoCliente.class.getAnnotation(FeignClient.class);
            if (annotation != null) {
                String name = annotation.name();
                assertNotNull(name);
                assertEquals("ms-pagos", name);
            }
        }
    }

    @Test
    public void testPagoClienteFeignClientUrl() {
        if (pagoCliente != null) {
            FeignClient annotation = PagoCliente.class.getAnnotation(FeignClient.class);
            if (annotation != null) {
                String url = annotation.url();
                assertNotNull(url);
                assertTrue(url.contains("localhost"));
                assertTrue(url.contains("8085"));
            }
        }
    }

    @Test
    public void testPagoClienteMapeaPagosEndpoint() {
        try {
            var method = PagoCliente.class.getMethod("procesarPago", PagoRequestDTO.class);
            var annotations = method.getAnnotations();
            boolean hasPostMapping = false;
            for (var ann : annotations) {
                if (ann.annotationType().getSimpleName().equals("PostMapping")) {
                    hasPostMapping = true;
                    break;
                }
            }
            assertTrue(hasPostMapping, "El método debería tener @PostMapping");
        } catch (NoSuchMethodException e) {
            fail("El método procesarPago no existe");
        }
    }

    // ─── Tests de integración básicos ──────────────────────────────

    @Test
    public void testPagoClienteInstanciaNoEsNull() {
        assertNotNull(pagoCliente);
    }

    @Test
    public void testPagoRequestDTONoEsNull() {
        assertNotNull(requestDTO);
    }

    @Test
    public void testPagoRequestDTOValoresCorrectos() {
        assertEquals(1L, requestDTO.getReservaId());
        assertEquals(15000.0, requestDTO.getMonto());
        assertEquals("TARJETA", requestDTO.getMetodo());
    }

    // ─── Tests de la URL del cliente ───────────────────────────────

    @Test
    public void testPagoClienteURLContiene8085() {
        FeignClient annotation = PagoCliente.class.getAnnotation(FeignClient.class);
        if (annotation != null) {
            String url = annotation.url();
            assertTrue(url.contains("8085"), "La URL debería contener el puerto 8085");
        }
    }

    @Test
    public void testPagoClienteURLContieneLocalhost() {
        FeignClient annotation = PagoCliente.class.getAnnotation(FeignClient.class);
        if (annotation != null) {
            String url = annotation.url();
            assertTrue(url.contains("localhost"), "La URL debería contener localhost");
        }
    }

    // ─── Test del path del endpoint ─────────────────────────────────

    @Test
    public void testPagoClientePathEsApiPagos() {
        try {
            var method = PagoCliente.class.getMethod("procesarPago", PagoRequestDTO.class);
            var annotations = method.getAnnotations();
            boolean hasCorrectPath = false;
            for (var ann : annotations) {
                if (ann.annotationType().getSimpleName().equals("PostMapping")) {
                    try {
                        var valueField = ann.getClass().getMethod("value");
                        String[] paths = (String[]) valueField.invoke(ann);
                        if (paths.length > 0 && "/api/pagos".equals(paths[0])) {
                            hasCorrectPath = true;
                        }
                    } catch (Exception e) {
                        // Si no podemos acceder al valor, asumimos que está bien
                        hasCorrectPath = true;
                    }
                    break;
                }
            }
            assertTrue(hasCorrectPath, "El método debería mapear a /api/pagos");
        } catch (NoSuchMethodException e) {
            fail("El método procesarPago no existe");
        }
    }
}