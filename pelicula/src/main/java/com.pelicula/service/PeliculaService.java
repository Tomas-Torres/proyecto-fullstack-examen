package com.pelicula.service;

import com.pelicula.dto.PeliculaRequestDTO;
import com.pelicula.dto.PeliculaResponseDTO;
import com.pelicula.model.Pelicula;
import com.pelicula.repository.PeliculaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeliculaService {

    private final PeliculaRepository peliculaRepository;
    private final ReservaCliente reservaCliente;

    private PeliculaResponseDTO mapToDTO(Pelicula pelicula) {
        return new PeliculaResponseDTO(
                pelicula.getId(),
                pelicula.getTitulo(),
                pelicula.getGenero(),
                pelicula.getClasificacion(),
                pelicula.getDuracionMin(),
                pelicula.getSinopsis(),
                pelicula.getActivo()
        );
    }

    private Pelicula mapToEntity(PeliculaRequestDTO dto) {
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(dto.getTitulo());
        pelicula.setGenero(dto.getGenero());
        pelicula.setClasificacion(dto.getClasificacion());
        pelicula.setDuracionMin(dto.getDuracionMin());
        pelicula.setSinopsis(dto.getSinopsis());
        return pelicula;
    }

    public PeliculaResponseDTO crear(PeliculaRequestDTO dto) {
        log.info("Intentando crear película con título: {}", dto.getTitulo());
        if (peliculaRepository.existsByTituloIgnoreCase(dto.getTitulo())) {
            log.warn("Creación rechazada - título duplicado: {}", dto.getTitulo());
            throw new IllegalArgumentException("Ya existe una película con el título: " + dto.getTitulo());
        }
        Pelicula pelicula = mapToEntity(dto);
        pelicula.setActivo(true);
        Pelicula guardada = peliculaRepository.save(pelicula);
        log.info("Película creada exitosamente con ID: {}", guardada.getId());
        return mapToDTO(guardada);
    }

    public List<PeliculaResponseDTO> obtenerTodas() {
        log.info("Consultando todo el catálogo de películas");
        return peliculaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PeliculaResponseDTO> obtenerActivas() {
        log.info("Consultando películas activas en cartelera");
        return peliculaRepository.findByActivoTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PeliculaResponseDTO> obtenerPorGenero(String genero) {
        log.info("Consultando películas activas del género: {}", genero);
        return peliculaRepository.findByGeneroAndActivoTrue(genero).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PeliculaResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando película con ID: {}", id);
        return peliculaRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<PeliculaResponseDTO> actualizar(Long id, PeliculaRequestDTO dto) {
        return peliculaRepository.findById(id).map(pelicula -> {
            if (!pelicula.getTitulo().equalsIgnoreCase(dto.getTitulo()) &&
                    peliculaRepository.existsByTituloIgnoreCase(dto.getTitulo())) {
                log.warn("Actualización rechazada - título duplicado: {}", dto.getTitulo());
                throw new IllegalArgumentException("Ya existe otra película con el título: " + dto.getTitulo());
            }

            pelicula.setTitulo(dto.getTitulo());
            pelicula.setGenero(dto.getGenero());
            pelicula.setClasificacion(dto.getClasificacion());
            pelicula.setDuracionMin(dto.getDuracionMin());
            pelicula.setSinopsis(dto.getSinopsis());

            Pelicula actualizada = peliculaRepository.save(pelicula);
            log.info("Película ID {} actualizada correctamente", actualizada.getId());
            return mapToDTO(actualizada);
        });
    }

    public boolean eliminar(Long id) {
        return peliculaRepository.findById(id).map(pelicula -> {

            // 1. Verificamos mediante Feign si hay reservas para esta película
            log.info("Verificando si la película ID {} tiene reservas activas...", id);
            List<Object> reservas = reservaCliente.obtenerReservasPorPelicula(id);

            if (reservas != null && !reservas.isEmpty()) {
                log.warn("Eliminación rechazada - la película ID {} tiene reservas asociadas", id);
                throw new IllegalStateException("No se puede eliminar la película porque ya tiene entradas vendidas.");
            }

            pelicula.setActivo(false);
            peliculaRepository.save(pelicula);
            log.info("Película ID {} desactivada (soft delete)", id);
            return true;

        }).orElseGet(() -> {
            log.warn("Eliminación fallida - película no encontrada con ID: {}", id);
            return false;
        });
    }

    public boolean existePelicula(Long id) {
        log.debug("Verificando existencia de película ID: {}", id);
        boolean existe = peliculaRepository.findById(id)
                .map(Pelicula::getActivo)
                .orElse(false);
        log.debug("Película ID {} activa: {}", id, existe);
        return existe;
    }
}