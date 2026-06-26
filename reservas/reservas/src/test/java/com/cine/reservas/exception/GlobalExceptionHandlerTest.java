package com.cine.reservas.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    // ─── handleValidation() ─────────────────────────────────────────

    @Test
    public void testHandleValidation_ConUnError() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "campo", "El campo es obligatorio");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // When
        ResponseEntity<Map<String, String>> response = handler.handleValidation(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals("El campo es obligatorio", body.get("campo"));
    }

    @Test
    public void testHandleValidation_ConMultiplesErrores() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError error1 = new FieldError("objectName", "usuario_id", "El usuario_id es obligatorio");
        FieldError error2 = new FieldError("objectName", "total", "El total debe ser mayor a 0");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));

        // When
        ResponseEntity<Map<String, String>> response = handler.handleValidation(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
        assertEquals("El usuario_id es obligatorio", body.get("usuario_id"));
        assertEquals("El total debe ser mayor a 0", body.get("total"));
    }

    @Test
    public void testHandleValidation_SinErrores() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        // When
        ResponseEntity<Map<String, String>> response = handler.handleValidation(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isEmpty());
    }

    @Test
    public void testHandleValidation_ConErrorConMensajeNulo() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "campo", null);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // When
        ResponseEntity<Map<String, String>> response = handler.handleValidation(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertNull(body.get("campo"));
    }

    // ─── handleRuntime() ────────────────────────────────────────────

    @Test
    public void testHandleRuntime() {
        // Given
        String mensaje = "Error inesperado en el sistema";
        RuntimeException exception = new RuntimeException(mensaje);

        // When
        ResponseEntity<Map<String, String>> response = handler.handleRuntime(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(mensaje, body.get("error"));
    }

    @Test
    public void testHandleRuntime_ConMensajeNulo() {
        // Given
        RuntimeException exception = new RuntimeException();

        // When
        ResponseEntity<Map<String, String>> response = handler.handleRuntime(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertNull(body.get("error"));
    }

    @Test
    public void testHandleRuntime_ConMensajeVacio() {
        // Given
        RuntimeException exception = new RuntimeException("");

        // When
        ResponseEntity<Map<String, String>> response = handler.handleRuntime(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals("", body.get("error"));
    }

    @Test
    public void testHandleRuntime_ConMensajeConEspacios() {
        // Given
        String mensaje = "  Error con espacios  ";
        RuntimeException exception = new RuntimeException(mensaje);

        // When
        ResponseEntity<Map<String, String>> response = handler.handleRuntime(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(mensaje, body.get("error"));
    }

    @Test
    public void testHandlerSiempreDevuelveBadRequest() {
        // Given
        RuntimeException exception = new RuntimeException("Error de prueba");

        // When
        ResponseEntity<Map<String, String>> response = handler.handleRuntime(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testHandlerNuncaDevuelveNull() {
        // Given
        RuntimeException exception = new RuntimeException();

        // When
        ResponseEntity<Map<String, String>> response = handler.handleRuntime(exception);

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getStatusCode());
    }

    @Test
    public void testValidacionHandlerSiempreDevuelveBadRequest() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "campo", "Error");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // When
        ResponseEntity<Map<String, String>> response = handler.handleValidation(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testHandlerExiste() {
        assertNotNull(handler);
    }
}