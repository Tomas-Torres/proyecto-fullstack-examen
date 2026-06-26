package com.usuario.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "ms-reservas", url = "http://ms-reservas:8085")
public interface ReservaCliente {

    @GetMapping("/api/reservas/usuario/{usuarioId}")
    List<Object> obtenerReservasPorUsuario(@PathVariable Long usuarioId);
}