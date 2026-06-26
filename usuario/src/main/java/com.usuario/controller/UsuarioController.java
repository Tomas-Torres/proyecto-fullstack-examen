package com.usuario.controller;

import com.usuario.assembler.UsuarioModelAssembler;
import com.usuario.dto.UsuarioRequestDTO;
import com.usuario.dto.UsuarioResponseDTO;
import com.usuario.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResponseDTO>>> obtenerTodos() {
        log.info("GET /api/usuario - Listando todos los usuarios");
        List<EntityModel<UsuarioResponseDTO>> usuarios = usuarioService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping("/activos")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResponseDTO>>> obtenerActivos() {
        log.info("GET /api/usuario/activos - Listando usuarios activos");
        List<EntityModel<UsuarioResponseDTO>> usuarios = usuarioService.obtenerActivos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).obtenerActivos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/usuario/{} - Buscando usuario por ID", id);
        return usuarioService.obtenerPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        log.info("POST /api/usuario - Creando nuevo usuario con email: {}", dto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(usuarioService.crearUsuario(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO dto) {
        log.info("PUT /api/usuario/{} - Actualizando usuario", id);
        return usuarioService.actualizar(id, dto)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/usuario/{} - Desactivando usuario", id);
        boolean eliminado = usuarioService.eliminar(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("mensaje", "Usuario desactivado correctamente"));
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Map<String, Boolean>> existeUsuario(@PathVariable Long id) {
        log.info("GET /api/usuario/{}/existe - Verificando existencia para MS-Reservas", id);
        return ResponseEntity.ok(Map.of("existe", usuarioService.existeUsuario(id)));
    }
}