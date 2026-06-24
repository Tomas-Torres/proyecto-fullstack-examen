package com.pelicula.service;

import com.pelicula.dto.PeliculaRequestDTO;
import com.pelicula.dto.PeliculaResponseDTO;
import com.pelicula.model.Pelicula;
import com.pelicula.repository.PeliculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculaServiceTest {

    @Mock
    private PeliculaRepository peliculaRepository;

    @InjectMocks
    private PeliculaService peliculaService;

    private Pelicula pelicula;
    private PeliculaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        pelicula = new Pelicula();
        pelicula.setId(1L);
        pelicula.setTitulo("Avengers");
        pelicula.setGenero("ACCION");
        pelicula.setClasificacion("PG-13");
        pelicula.setDuracionMin(180);
        pelicula.setSinopsis("Historia de superhéroes");
        pelicula.setActivo(true);

        requestDTO = new PeliculaRequestDTO(
                "Avengers",
                "ACCION",
                "PG-13",
                180,
                "Historia de superhéroes"
        );
    }

    @Test
    void crear_exitoso() {
        when(peliculaRepository.existsByTituloIgnoreCase(requestDTO.getTitulo())).thenReturn(false);
        when(peliculaRepository.save(any(Pelicula.class))).thenReturn(pelicula);

        PeliculaResponseDTO resultado = peliculaService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Avengers", resultado.getTitulo());
        verify(peliculaRepository, times(1)).save(any(Pelicula.class));
    }

    @Test
    void crear_tituloDuplicado_lanzaExcepcion() {
        when(peliculaRepository.existsByTituloIgnoreCase(requestDTO.getTitulo())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> peliculaService.crear(requestDTO));

        verify(peliculaRepository, never()).save(any());
    }

    @Test
    void obtenerTodas_retornaLista() {
        when(peliculaRepository.findAll()).thenReturn(List.of(pelicula));

        List<PeliculaResponseDTO> resultado = peliculaService.obtenerTodas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Avengers", resultado.get(0).getTitulo());
    }

    @Test
    void obtenerTodas_listaVacia() {
        when(peliculaRepository.findAll()).thenReturn(List.of());

        List<PeliculaResponseDTO> resultado = peliculaService.obtenerTodas();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void obtenerActivas_retornaSoloActivas() {
        when(peliculaRepository.findByActivoTrue()).thenReturn(List.of(pelicula));

        List<PeliculaResponseDTO> resultado = peliculaService.obtenerActivas();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.get(0).getActivo());
    }

    @Test
    void obtenerPorGenero_retornaFiltradas() {
        when(peliculaRepository.findByGeneroAndActivoTrue("ACCION")).thenReturn(List.of(pelicula));