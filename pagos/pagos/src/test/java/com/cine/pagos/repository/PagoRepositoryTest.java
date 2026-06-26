package com.cine.pagos.repository;

import com.cine.pagos.model.Pago;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PagoRepositoryTest {

    @Autowired
    private PagoRepository pagoRepository;

    private Pago pago;

    @BeforeEach
    void setUp() {
        pagoRepository.deleteAll();

        pago = new Pago();
        pago.setReservaId(1L);
        pago.setMonto(15000.0);
        pago.setMetodo("TARJETA");
        pago.setEstado("PROCESANDO");
        pago.setCodigoTransaccion("TXN-" + System.currentTimeMillis());
        pago.setFechaPago(LocalDateTime.now());
        pago.setMensajeError(null);
    }

    @Test
    public void testSave() {
        Pago saved = pagoRepository.save(pago);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(1L, saved.getReservaId());
        assertEquals(15000.0, saved.getMonto());
        assertEquals("TARJETA", saved.getMetodo());
        assertEquals("PROCESANDO", saved.getEstado());
        assertNotNull(saved.getCodigoTransaccion());
        assertNotNull(saved.getFechaPago());
    }

    @Test
    public void testFindById_CuandoExiste() {
        Pago saved = pagoRepository.save(pago);
        Optional<Pago> found = pagoRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    public void testFindById_CuandoNoExiste() {
        Optional<Pago> found = pagoRepository.findById(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    public void testFindAll() {
        pagoRepository.save(pago);
        List<Pago> pagos = pagoRepository.findAll();
        assertNotNull(pagos);
        assertFalse(pagos.isEmpty());
        assertEquals(1, pagos.size());
    }

    @Test
    public void testFindAll_CuandoNoHayPagos() {
        List<Pago> pagos = pagoRepository.findAll();
        assertNotNull(pagos);
        assertTrue(pagos.isEmpty());
    }

    @Test
    public void testFindAll_ConMultiplesPagos() {
        Pago pago2 = new Pago();
        pago2.setReservaId(2L);
        pago2.setMonto(25000.0);
        pago2.setMetodo("EFECTIVO");
        pago2.setEstado("APROBADO");
        pago2.setCodigoTransaccion("TXN-" + System.currentTimeMillis() + "-2");
        pago2.setFechaPago(LocalDateTime.now());

        pagoRepository.save(pago);
        pagoRepository.save(pago2);
        List<Pago> pagos = pagoRepository.findAll();
        assertEquals(2, pagos.size());
    }

    @Test
    public void testFindByReservaId_CuandoExiste() {
        Pago saved = pagoRepository.save(pago);
        Optional<Pago> found = pagoRepository.findByReservaId(saved.getReservaId());
        assertTrue(found.isPresent());
        assertEquals(saved.getReservaId(), found.get().getReservaId());
    }

    @Test
    public void testFindByReservaId_CuandoNoExiste() {
        Optional<Pago> found = pagoRepository.findByReservaId(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    public void testExistsById_CuandoExiste() {
        Pago saved = pagoRepository.save(pago);
        boolean existe = pagoRepository.existsById(saved.getId());
        assertTrue(existe);
    }

    @Test
    public void testExistsById_CuandoNoExiste() {
        boolean existe = pagoRepository.existsById(999L);
        assertFalse(existe);
    }

    @Test
    public void testDeleteById() {
        Pago saved = pagoRepository.save(pago);
        pagoRepository.deleteById(saved.getId());
        Optional<Pago> found = pagoRepository.findById(saved.getId());
        assertTrue(found.isEmpty());
    }

    @Test
    public void testCount() {
        pagoRepository.save(pago);
        long count = pagoRepository.count();
        assertEquals(1, count);
    }

    @Test
    public void testCount_CuandoNoHayPagos() {
        long count = pagoRepository.count();
        assertEquals(0, count);
    }

    @Test
    public void testSave_EstadoAprobado() {
        Pago pagoAprobado = new Pago();
        pagoAprobado.setReservaId(6L);
        pagoAprobado.setMonto(15000.0);
        pagoAprobado.setMetodo("TARJETA");
        pagoAprobado.setEstado("APROBADO");
        pagoAprobado.setCodigoTransaccion("TXN-APROBADO-001");
        pagoAprobado.setFechaPago(LocalDateTime.now());

        Pago saved = pagoRepository.save(pagoAprobado);
        assertEquals("APROBADO", saved.getEstado());
    }

    @Test
    public void testSave_EstadoRechazado() {
        Pago pagoRechazado = new Pago();
        pagoRechazado.setReservaId(7L);
        pagoRechazado.setMonto(15000.0);
        pagoRechazado.setMetodo("TARJETA");
        pagoRechazado.setEstado("RECHAZADO");
        pagoRechazado.setCodigoTransaccion("TXN-RECHAZADO-001");
        pagoRechazado.setFechaPago(LocalDateTime.now());
        pagoRechazado.setMensajeError("Fondos insuficientes");

        Pago saved = pagoRepository.save(pagoRechazado);
        assertEquals("RECHAZADO", saved.getEstado());
        assertEquals("Fondos insuficientes", saved.getMensajeError());
    }

    @Test
    public void testSave_EstadoTimeout() {
        Pago pagoTimeout = new Pago();
        pagoTimeout.setReservaId(8L);
        pagoTimeout.setMonto(15000.0);
        pagoTimeout.setMetodo("TARJETA");
        pagoTimeout.setEstado("TIMEOUT");
        pagoTimeout.setCodigoTransaccion("TXN-TIMEOUT-001");
        pagoTimeout.setFechaPago(LocalDateTime.now());
        pagoTimeout.setMensajeError("Tiempo de espera agotado");

        Pago saved = pagoRepository.save(pagoTimeout);
        assertEquals("TIMEOUT", saved.getEstado());
        assertEquals("Tiempo de espera agotado", saved.getMensajeError());
    }

    @Test
    public void testUpdate() {
        Pago saved = pagoRepository.save(pago);
        saved.setEstado("APROBADO");
        saved.setMensajeError(null);
        Pago updated = pagoRepository.save(saved);
        assertEquals("APROBADO", updated.getEstado());
        assertNull(updated.getMensajeError());
    }
}