package com.usuario.controller;

import com.usuario.dto.UsuarioRequestDTO;
import com.usuario.dto.UsuarioResponseDTO;
import com.usuario.service.UsuarioService;
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
@RequestMapping("/api/usuario") // Corregido a singular para coincidir con tus paquetes
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // GET /api/usuario → Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos() {
        log.info("GET /api/usuario - Listando todos los usuarios");
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    // GET /api/usuario/activos → Listar solo usuarios activos
    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerActivos() {
        log.info("GET /api/usuario/activos - Listando usuarios activos");
        return ResponseEntity.ok(usuarioService.obtenerActivos());
    }

    // GET /api/usuario/{id} → Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/usuario/{} - Buscando usuario por ID", id);
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/usuario → Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        log.info("POST /api/usuario - Creando nuevo usuario con email: {}", dto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crearUsuario(dto));
    }

    // PUT /api/usuario/{id} → Actualizar datos del usuario
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO dto) {
        log.info("PUT /api/usuario/{} - Actualizando usuario", id);
        return usuarioService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/usuario/{id} → Desactivar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/usuario/{} - Desactivando usuario", id);
        boolean eliminado = usuarioService.eliminar(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("mensaje", "Usuario desactivado correctamente"));
    }

    // GET /api/usuario/{id}/existe → Validación interna para Reservas
    @GetMapping("/{id}/existe")
    public ResponseEntity<Map<String, Boolean>> existeUsuario(@PathVariable Long id) {
        log.info("GET /api/usuario/{}/existe - Verificando existencia para MS-Reservas", id);
        return ResponseEntity.ok(Map.of("existe", usuarioService.existeUsuario(id)));
    }
}