package com.usuario.service;

import com.usuario.dto.UsuarioRequestDTO;
import com.usuario.dto.UsuarioResponseDTO;
import com.usuario.model.Usuario;
import com.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setEmail("juan@email.com");
        usuario.setPassword("123456");
        usuario.setRol(Usuario.Rol.CLIENTE);
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        requestDTO = new UsuarioRequestDTO(
                "Juan",
                "Pérez",
                "juan@email.com",
                "123456",
                Usuario.Rol.CLIENTE
        );
    }

    @Test
    void crearUsuario_exitoso() {
        when(usuarioRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = usuarioService.crearUsuario(requestDTO);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("juan@email.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_emailDuplicado_lanzaExcepcion() {
        when(usuarioRepository.existsByEmail(requestDTO.getEmail())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.crearUsuario(requestDTO));

        assertEquals("Ya existe un usuario con el email: juan@email.com", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void obtenerTodos_retornaLista() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.obtenerTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerActivos_retornaSoloActivos() {
        when(usuarioRepository.findByActivoTrue()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.obtenerActivos();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.get(0).getActivo());
    }

    @Test
    void obtenerPorId_encontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<UsuarioResponseDTO> resultado = usuarioService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void obtenerPorId_noEncontrado() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UsuarioResponseDTO> resultado = usuarioService.obtenerPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void eliminar_exitoso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        boolean resultado = usuarioService.eliminar(1L);

        assertTrue(resultado);
        assertFalse(usuario.getActivo());
    }

    @Test
    void eliminar_usuarioNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        boolean resultado = usuarioService.eliminar(99L);

        assertFalse(resultado);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void existeUsuario_activoRetornaTrue() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        boolean resultado = usuarioService.existeUsuario(1L);

        assertTrue(resultado);
    }

    @Test
    void existeUsuario_inactivoRetornaFalse() {
        usuario.setActivo(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        boolean resultado = usuarioService.existeUsuario(1L);

        assertFalse(resultado);
    }

    @Test
    void existeUsuario_noExisteRetornaFalse() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        boolean resultado = usuarioService.existeUsuario(99L);

        assertFalse(resultado);
    }
}