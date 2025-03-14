package com.wilson.api_meteorologica.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilson.api_meteorologica.entity.AuditConsultation;
import com.wilson.api_meteorologica.repository.AuditConsultatiorepository;
import com.wilson.api_meteorologica.security.SecurityService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
@Service
public class AuditService {
    private final AuditConsultatiorepository consultatiorepository;
    private final ObjectMapper objectMapper;
    private final SecurityService securityService;
    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    public AuditService(AuditConsultatiorepository consultatiorepository,
                        ObjectMapper objectMapper,
                        SecurityService securityService) {
        this.consultatiorepository = consultatiorepository;
        this.objectMapper = objectMapper;
        this.securityService = securityService;
    }

    /**
     * Registra una consulta meteorológica en la base de datos para auditoría.
     * Se almacena el usuario autenticado, el tipo de consulta y la respuesta.
     *
     * @param queryType      Tipo de consulta (ej. "currentWeather", "forecastWeather").
     * @param city           Ciudad consultada.
     * @param responseObject Respuesta de la API almacenada en formato JSON.
     */
    @Transactional
    public void registerQuery(String queryType, String city, Object responseObject) {
        String username = securityService.getAuthenticatedUser(); // obtiene el usuario real

        String responseJson = "{}";
        try {
            responseJson = objectMapper.writeValueAsString(responseObject);
        } catch (JsonProcessingException e) {
            logger.error("Error convirtiendo la respuesta a JSON para auditoría: {}", e.getMessage());
        }

        AuditConsultation query = new AuditConsultation(username, queryType, city, LocalDateTime.now(), responseJson);
        consultatiorepository.save(query);

        logger.info("Auditoría registrada para usuario '{}' en consulta '{}' de la ciudad '{}'", username, queryType, city);
    }
}
