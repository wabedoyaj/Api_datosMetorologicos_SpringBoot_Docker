package com.wilson.api_meteorologica.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
/**
 * Configura Swagger para generar documentación automática de la API.
 * - Define el título, versión y descripción de la API.
 * - Habilita la autenticación con JWT en la documentación.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(title = "API Meteorológica", version = "1.0", description = "API para obtener datos meteorológicos"),
        security = @SecurityRequirement(name = "bearerAuth") // Aplica autenticación JWT
)
@SecurityScheme(
        name = "bearerAuth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
