package com.pelicula.controller;

import com.pelicula.assembler.PeliculaModelAssembler;
import com.pelicula.dto.PeliculaRequestDTO;
import com.pelicula.dto.PeliculaResponseDTO;
import com.pelicula.service.PeliculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/peliculas")
@RequiredArgsConstructor
public class PeliculaController {

    private final PeliculaService peliculaService;
    private final PeliculaModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PeliculaResponseDTO>>> obtenerTodas() {
        log.info("GET /api/peliculas - Listando catálogo completo");
        List<EntityModel<PeliculaResponseDTO>> peliculas = peliculaService.obtenerTodas()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(peliculas,
                linkTo(methodOn(PeliculaController.class).obtenerTodas()).withSelfRel()));
    }

    @GetMapping("/activas")
    public ResponseEntity<CollectionModel<EntityModel<PeliculaResponseDTO>>> obtenerActivas() {
        log.info("GET /api/peliculas/activas - Listando películas activas");
        List<EntityModel<PeliculaResponseDTO>> peliculas = peliculaService.obtenerActivas()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(peliculas,
                linkTo(methodOn(PeliculaController.class).obtenerActivas()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PeliculaResponseDTO>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/peliculas/{} - Buscando película por ID", id);
        return peliculaService.obtenerPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<PeliculaResponseDTO>> crear(@Valid @RequestBody PeliculaRequestDTO dto) {
        log.info("POST /api/peliculas - Agregando nueva película: {}", dto.getTitulo());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(peliculaService.crear(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PeliculaResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PeliculaRequestDTO dto) {
        log.info("PUT /api/peliculas/{} - Actualizando película", id);
        return peliculaService.actualizar(id, dto)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/peliculas/{} - Dando de baja película", id);
        boolean eliminada = peliculaService.eliminar(id);
        if (!eliminada) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("mensaje", "Película retirada del catálogo exitosamente"));
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Map<String, Boolean>> existePelicula(@PathVariable Long id) {
        log.info("GET /api/peliculas/{}/existe - Verificando existencia", id);
        return ResponseEntity.ok(Map.of("existe", peliculaService.existePelicula(id)));
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<CollectionModel<EntityModel<PeliculaResponseDTO>>> obtenerPorGenero(
            @PathVariable String genero) {
        log.info("GET /api/peliculas/genero/{} - Filtrando por género", genero);
        List<EntityModel<PeliculaResponseDTO>> peliculas = peliculaService.obtenerPorGenero(genero)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(peliculas,
                linkTo(methodOn(PeliculaController.class).obtenerPorGenero(genero)).withSelfRel()));
    }
}