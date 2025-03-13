package com.wilson.api_meteorologica.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilson.api_meteorologica.entity.AuditConsultation;
import com.wilson.api_meteorologica.repository.AuditConsultatiorepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class AuditService {
    @Autowired
    private AuditConsultatiorepository consultatiorepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Registra una consulta meteorológica en la base de datos para auditoría.
     * Se almacena el usuario que hizo la consulta, el tipo de consulta y la ciudad consultada.
     *
     * @param username       Nombre del usuario que realizó la consulta.
     * @param queryType      Tipo de consulta (ej. "currentWeather", "forecastWeather").
     * @param city           Ciudad consultada.
     * @param responseObject Respuesta de la API almacenada en formato JSON.
     */
    @Transactional
    public void registerQuery(String username, String queryType, String city, Object responseObject) {
        String responseJson ="{}";
        try {
            responseJson = objectMapper.writeValueAsString(responseObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        AuditConsultation query = new AuditConsultation(username,queryType,city, LocalDateTime.now(),responseJson);
        consultatiorepository.save(query);
    }
}
