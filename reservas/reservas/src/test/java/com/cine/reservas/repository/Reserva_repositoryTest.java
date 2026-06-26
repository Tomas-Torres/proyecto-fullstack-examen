package com.cine.reservas.repository;

import com.cine.reservas.model.Resrvas_model;
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
public class Reserva_repositoryTest {

    @Autowired
    private Reserva_repository reservaRepository;

    private Resrvas_model reserva;

    @BeforeEach
    void setUp() {
        reservaRepository.deleteAll(); // ← limpia la BD antes de cada test

        reserva = new Resrvas_model();
        reserva.setUsuarioId(1L);
        reserva.setFuncionId(1L);
        reserva.setCantidadDeAsientos(2);
        reserva.setTotal(15000.0);
        reserva.setEstado("PENDIENTE");
        reserva.setFechaReserva(LocalDateTime.now());
    }

    @Test
    public void testSave() {
        Resrvas_model saved = reservaRepository.save(reserva);
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void testFindById_CuandoExiste() {
        Resrvas_model saved = reservaRepository.save(reserva);
        Optional<Resrvas_model> found = reservaRepository.findById(saved.getId());
        assertTrue(found.isPresent());
    }

    @Test
    public void testFindById_CuandoNoExiste() {
        Optional<Resrvas_model> found = reservaRepository.findById(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    public void testFindAll() {
        reservaRepository.save(reserva);
        List<Resrvas_model> reservas = reservaRepository.findAll();
        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
    }

    @Test
    public void testFindByUsuarioId() {
        reservaRepository.save(reserva);
        List<Resrvas_model> reservas = reservaRepository.findByUsuarioId(1L);
        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
    }

    @Test
    public void testFindByUsuarioId_CuandoNoHayReservas() {
        List<Resrvas_model> reservas = reservaRepository.findByUsuarioId(999L);
        assertNotNull(reservas);
        assertTrue(reservas.isEmpty());
    }

    @Test
    public void testFindByFuncionId() {
        reservaRepository.save(reserva);
        List<Resrvas_model> reservas = reservaRepository.findByFuncionId(1L);
        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
    }

    @Test
    public void testFindByFuncionId_CuandoNoHayReservas() {
        List<Resrvas_model> reservas = reservaRepository.findByFuncionId(999L);
        assertNotNull(reservas);
        assertTrue(reservas.isEmpty());
    }

    @Test
    public void testFindByEstado() {
        reservaRepository.save(reserva);
        List<Resrvas_model> reservas = reservaRepository.findByEstado("PENDIENTE");
        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
    }

    @Test
    public void testFindByEstado_CuandoNoHayReservas() {
        List<Resrvas_model> reservas = reservaRepository.findByEstado("CANCELADA");
        assertNotNull(reservas);
        assertTrue(reservas.isEmpty());
    }

    @Test
    public void testExistsById_CuandoExiste() {
        Resrvas_model saved = reservaRepository.save(reserva);
        boolean existe = reservaRepository.existsById(saved.getId());
        assertTrue(existe);
    }

    @Test
    public void testExistsById_CuandoNoExiste() {
        boolean existe = reservaRepository.existsById(999L);
        assertFalse(existe);
    }

    @Test
    public void testDeleteById() {
        Resrvas_model saved = reservaRepository.save(reserva);
        reservaRepository.deleteById(saved.getId());
        Optional<Resrvas_model> found = reservaRepository.findById(saved.getId());
        assertTrue(found.isEmpty());
    }

    @Test
    public void testCount() {
        reservaRepository.save(reserva);
        long count = reservaRepository.count();
        assertEquals(1, count); // ← ahora siempre será 1
    }

    @Test
    public void testFindByUsuarioIdAndEstado() {
        reservaRepository.save(reserva);
        List<Resrvas_model> reservas = reservaRepository.findByUsuarioIdAndEstado(1L, "PENDIENTE");
        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
    }
}