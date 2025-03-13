package com.wilson.api_meteorologica.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
/**
 * Maneja los errores de autenticación cuando un usuario intenta acceder a recursos protegidos sin estar autenticado.
 * Implementa AuthenticationEntryPoint para personalizar la respuesta de error HTTP 401 (No autorizado).
 */
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    /**
     * Se ejecuta cuando un usuario no autenticado intenta acceder a un recurso protegido.
     * Devuelve una respuesta en formato JSON con detalles del error.
     * @param req  Solicitud HTTP.
     * @param res  Respuesta HTTP.
     * @param e    Excepción de autenticación capturada.
     */
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e)
            throws IOException, ServletException {
        logger.error("Error en el metodo commence");

        res.setContentType("application/json");
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Crear la respuesta en formato JSON con detalles del error
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now().toString());
        errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        errorDetails.put("error", "Unauthorized");
        errorDetails.put("message", "No autorizado");
        errorDetails.put("path", req.getRequestURI());

        // Convertir la respuesta a JSON y enviarla
        ObjectMapper objectMapper = new ObjectMapper();
        res.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
