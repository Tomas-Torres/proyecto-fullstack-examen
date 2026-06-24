package com.pelicula.controller;

import com.pelicula.dto.PeliculaRequestDTO;
import com.pelicula.dto.PeliculaResponseDTO;
import com.pelicula.service.PeliculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/peliculas")
@RequiredArgsConstructor
public class PeliculaController {

    private final PeliculaService peliculaService;

    // GET /api/peliculas → Obtener catálogo completo
    @GetMapping
    public ResponseEntity<List<PeliculaResponseDTO>> obtenerTodas() {
        log.info("GET /api/peliculas - Listando catálogo completo");
        return ResponseEntity.ok(peliculaService.obtenerTodas());
    }

    // GET /api/peliculas/activas → Listar solo películas en cartelera
    @GetMapping("/activas")
    public ResponseEntity<List<PeliculaResponseDTO>> obtenerActivas() {
        log.info("GET /api/peliculas/activas - Listando películas activas");
        return ResponseEntity.ok(peliculaService.obtenerActivas());
    }

    // GET /api/peliculas/{id} → Detalle de una película
    @GetMapping("/{id}")
    public ResponseEntity<PeliculaResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/peliculas/{} - Buscando película por ID", id);
        return peliculaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/peliculas → Agregar película al catálogo
    @PostMapping
    public ResponseEntity<PeliculaResponseDTO> crear(@Valid @RequestBody PeliculaRequestDTO dto) {
        log.info("POST /api/peliculas - Agregando nueva película: {}", dto.getTitulo());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(peliculaService.crear(dto));
    }

    // PUT /api/peliculas/{id} → Actualizar película existente
    @PutMapping("/{id}")
    public ResponseEntity<PeliculaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PeliculaRequestDTO dto) {
        log.info("PUT /api/peliculas/{} - Actualizando película", id);
        return peliculaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/peliculas/{id} → Retirar película (Soft Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/peliculas/{} - Dando de baja película", id);
        boolean eliminada = peliculaService.eliminar(id);
        if (!eliminada) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("mensaje", "Película retirada del catálogo exitosamente"));
    }

    // GET /api/peliculas/{id}/existe → Validación para
    @GetMapping("/{id}/existe")
    public ResponseEntity<Map<String, Boolean>> existePelicula(@PathVariable Long id) {
        log.info("GET /api/peliculas/{}/existe - Verificando existencia ", id);
        return ResponseEntity.ok(Map.of("existe", peliculaService.existePelicula(id)));
    }
}