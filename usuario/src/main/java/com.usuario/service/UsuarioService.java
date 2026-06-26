package com.usuario.service;

import com.usuario.dto.UsuarioRequestDTO;
import com.usuario.dto.UsuarioResponseDTO;
import com.usuario.model.Usuario;
import com.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private UsuarioResponseDTO mapToDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getActivo(),
                usuario.getFechaRegistro()
        );
    }

    private Usuario mapToEntity(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());
        return usuario;
    }

    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {
        log.info("Intentando crear usuario con email: {}", dto.getEmail());
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            log.warn("Creación rechazada - email ya registrado: {}", dto.getEmail());
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + dto.getEmail());
        }
        Usuario guardado = usuarioRepository.save(mapToEntity(dto));
        log.info("Usuario creado exitosamente con ID: {}", guardado.getId());
        return mapToDTO(guardado);
    }

    public List<UsuarioResponseDTO> obtenerTodos() {
        log.info("Consultando todos los usuarios del sistema");
        return usuarioRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> obtenerActivos() {
        log.info("Consultando usuarios activos");
        return usuarioRepository.findByActivoTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        log.debug("Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<UsuarioResponseDTO> actualizar(Long id, UsuarioRequestDTO dto) {
        log.info("Actualizando usuario con ID: {}", id);
        return usuarioRepository.findById(id).map(usuario -> {
            if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
                log.warn("Actualización rechazada - email en uso: {}", dto.getEmail());
                throw new IllegalArgumentException("El email ya está en uso: " + dto.getEmail());
            }
            usuario.setNombre(dto.getNombre());
            usuario.setApellido(dto.getApellido());
            usuario.setEmail(dto.getEmail());
            usuario.setPassword(dto.getPassword());
            usuario.setRol(dto.getRol());
            Usuario actualizado = usuarioRepository.save(usuario);
            log.info("Usuario ID {} actualizado correctamente", actualizado.getId());
            return mapToDTO(actualizado);
        });
    }

    public boolean eliminar(Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
            log.info("Usuario ID {} desactivado (soft delete)", id);
            return true;
        }).orElseGet(() -> {
            log.warn("Eliminación fallida - usuario no encontrado con ID: {}", id);
            return false;
        });
    }

    public boolean existeUsuario(Long id) {
        log.debug("Verificando existencia del usuario ID: {}", id);
        boolean existe = usuarioRepository.findById(id)
                .map(Usuario::getActivo)
                .orElse(false);
        log.debug("Usuario ID {} activo: {}", id, existe);
        return existe;
    }
}