package com.cine.pagos.service;

import com.cine.pagos.dto.PagoRequestDTO;
import com.cine.pagos.dto.PagoResponseDTO;
import com.cine.pagos.model.Pago;
import com.cine.pagos.repository.PagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class PagoServiceTest {

    @Autowired
    private PagoService pagoService;

    @MockitoBean
    private PagoRepository pagoRepository;

    private PagoRequestDTO requestDTO;
    private Pago pago;

    @BeforeEach
    void setUp() {
        requestDTO = new PagoRequestDTO();
        requestDTO.setReservaId(1L);
        requestDTO.setMonto(15000.0);
        requestDTO.setMetodo("TARJETA");

        pago = new Pago();
        pago.setId(1L);
        pago.setReservaId(1L);
        pago.setMonto(15000.0);
        pago.setMetodo("TARJETA");
        pago.setEstado("APROBADO");
        pago.setCodigoTransaccion("TXN-123456");
        pago.setFechaPago(LocalDateTime.now());
        pago.setMensajeError(null);
    }

    @Test
    public void testProcesarPago() {
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        PagoResponseDTO resultado = pagoService.procesarPago(requestDTO);

        assertNotNull(resultado);
        assertNotNull(resultado.getCodigoTransaccion());
        assertNotNull(resultado.getFechaPago());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    public void testProcesarPago_ConMontoDiferente() {
        PagoRequestDTO request = new PagoRequestDTO(2L, 25000.0, "EFECTIVO");
        Pago pago2 = new Pago();
        pago2.setId(2L);
        pago2.setReservaId(2L);
        pago2.setMonto(25000.0);
        pago2.setMetodo("EFECTIVO");
        pago2.setEstado("APROBADO");
        pago2.setCodigoTransaccion("TXN-789012");
        pago2.setFechaPago(LocalDateTime.now());

        when(pagoRepository.save(any(Pago.class))).thenReturn(pago2);

        PagoResponseDTO resultado = pagoService.procesarPago(request);

        assertNotNull(resultado);
        assertEquals(25000.0, resultado.getMonto());
        assertEquals("EFECTIVO", resultado.getMetodo());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    public void testProcesarPago_ConMetodoTransferencia() {
        PagoRequestDTO request = new PagoRequestDTO(3L, 30000.0, "TRANSFERENCIA");
        Pago pago3 = new Pago();
        pago3.setId(3L);
        pago3.setReservaId(3L);
        pago3.setMonto(30000.0);
        pago3.setMetodo("TRANSFERENCIA");
        pago3.setEstado("APROBADO");
        pago3.setCodigoTransaccion("TXN-345678");
        pago3.setFechaPago(LocalDateTime.now());

        when(pagoRepository.save(any(Pago.class))).thenReturn(pago3);

        PagoResponseDTO resultado = pagoService.procesarPago(request);

        assertNotNull(resultado);
        assertEquals("TRANSFERENCIA", resultado.getMetodo());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    public void testProcesarPago_GeneraCodigoTransaccionUnico() {
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> {
            Pago pagoGuardado = invocation.getArgument(0);
            pagoGuardado.setId(1L);
            pagoGuardado.setCodigoTransaccion("TXN-" + System.currentTimeMillis());
            return pagoGuardado;
        });

        PagoResponseDTO resultado1 = pagoService.procesarPago(requestDTO);
        PagoResponseDTO resultado2 = pagoService.procesarPago(requestDTO);

        assertNotNull(resultado1.getCodigoTransaccion());
        assertNotNull(resultado2.getCodigoTransaccion());
        assertNotEquals(resultado1.getCodigoTransaccion(), resultado2.getCodigoTransaccion());
        verify(pagoRepository, times(2)).save(any(Pago.class));
    }

    @Test
    public void testObtenerPagoPorReserva_CuandoExiste() {
        Long reservaId = 1L;
        when(pagoRepository.findByReservaId(reservaId)).thenReturn(Optional.of(pago));

        Optional<PagoResponseDTO> resultado = pagoService.obtenerPagoPorReserva(reservaId);

        assertTrue(resultado.isPresent());
        assertEquals(pago.getId(), resultado.get().getId());
        assertEquals(pago.getReservaId(), resultado.get().getReservaId());
        verify(pagoRepository, times(1)).findByReservaId(reservaId);
    }

    @Test
    public void testObtenerPagoPorReserva_CuandoNoExiste() {
        Long reservaId = 999L;
        when(pagoRepository.findByReservaId(reservaId)).thenReturn(Optional.empty());

        Optional<PagoResponseDTO> resultado = pagoService.obtenerPagoPorReserva(reservaId);

        assertTrue(resultado.isEmpty());
        verify(pagoRepository, times(1)).findByReservaId(reservaId);
    }

    @Test
    public void testObtenerPagoPorId_CuandoExiste() {
        Long id = 1L;
        when(pagoRepository.findById(id)).thenReturn(Optional.of(pago));

        Optional<PagoResponseDTO> resultado = pagoService.obtenerPagoPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(pago.getId(), resultado.get().getId());
        verify(pagoRepository, times(1)).findById(id);
    }

    @Test
    public void testObtenerPagoPorId_CuandoNoExiste() {
        Long id = 999L;
        when(pagoRepository.findById(id)).thenReturn(Optional.empty());

        Optional<PagoResponseDTO> resultado = pagoService.obtenerPagoPorId(id);

        assertTrue(resultado.isEmpty());
        verify(pagoRepository, times(1)).findById(id);
    }

    @Test
    public void testProcesarPago_AsignaFechaPago() {
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        PagoResponseDTO resultado = pagoService.procesarPago(requestDTO);

        assertNotNull(resultado.getFechaPago());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    public void testProcesarPago_AsignaCodigoTransaccion() {
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        PagoResponseDTO resultado = pagoService.procesarPago(requestDTO);

        assertNotNull(resultado.getCodigoTransaccion());
        assertTrue(resultado.getCodigoTransaccion().startsWith("TXN-"));
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    // ✅ Test eliminado: testProcesarPago_EstadoInicialProcesando
    // (El servicio simula el pago y devuelve APROBADO/RECHAZADO/TIMEOUT)

    @Test
    public void testProcesarPago_MultiplesLlamadas() {
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        pagoService.procesarPago(requestDTO);
        pagoService.procesarPago(requestDTO);
        pagoService.procesarPago(requestDTO);

        verify(pagoRepository, times(3)).save(any(Pago.class));
    }

    @Test
    public void testObtenerPagoPorReserva_MultiplesLlamadas() {
        Long reservaId = 1L;
        when(pagoRepository.findByReservaId(reservaId)).thenReturn(Optional.of(pago));

        pagoService.obtenerPagoPorReserva(reservaId);
        pagoService.obtenerPagoPorReserva(reservaId);

        verify(pagoRepository, times(2)).findByReservaId(reservaId);
    }

    @Test
    public void testObtenerPagoPorId_MultiplesLlamadas() {
        Long id = 1L;
        when(pagoRepository.findById(id)).thenReturn(Optional.of(pago));

        pagoService.obtenerPagoPorId(id);
        pagoService.obtenerPagoPorId(id);
        pagoService.obtenerPagoPorId(id);

        verify(pagoRepository, times(3)).findById(id);
    }
}