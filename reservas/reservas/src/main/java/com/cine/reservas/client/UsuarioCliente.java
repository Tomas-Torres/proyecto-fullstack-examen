package com.cine.reservas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "ms-usuarios")
public interface UsuarioCliente {

    @GetMapping("/api/usuarios/{id}/existe")
    Map<String, Boolean> validarUsuario(@PathVariable("id") Long id);
}