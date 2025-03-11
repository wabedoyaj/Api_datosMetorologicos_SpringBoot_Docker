package com.wilson.api_meteorologica.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para personalizar la serialización y deserialización de JSON en la API.
 * Registra el módulo JavaTimeModule para manejar correctamente objetos de tipo LocalDate y LocalDateTime.
 */
@Configuration
public class JsonConfig {

    /**
     * Configura y proporciona un ObjectMapper con soporte para fechas.
     * Se usa en toda la aplicación para convertir objetos a JSON y viceversa.
     * @return ObjectMapper configurado con JavaTimeModule.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }
}
