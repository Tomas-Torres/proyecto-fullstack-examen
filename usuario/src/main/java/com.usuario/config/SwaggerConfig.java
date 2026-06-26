package com.usuario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS-Usuarios API")
                        .version("1.0")
                        .description("Microservicio de gestión de usuarios del sistema de cine")
                        .contact(new Contact()
                                .name("Cine App")
                                .email("admin@cine.com")));
    }
}