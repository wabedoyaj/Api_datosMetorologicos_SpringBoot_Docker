package com.wilson.api_meteorologica.controller;



import com.wilson.api_meteorologica.DTO.AirQualityDTO;
import com.wilson.api_meteorologica.DTO.WeatherDTO;

import com.wilson.api_meteorologica.DTO.WeatherForecastDTO;
import com.wilson.api_meteorologica.security.SecurityService;
import com.wilson.api_meteorologica.service.RateLimiterService;
import com.wilson.api_meteorologica.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Proporciona información del clima y calidad del aire.
 */
@RestController
@RequestMapping("/api/weather")
@Tag(name = "Clima", description = "Endpoints relacionados con el clima")
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación en todo el controlador
public class WeatherController {

    private final WeatherService weatherService;
    private final SecurityService securityService;
    private final RateLimiterService rateLimiterService;

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    public WeatherController(WeatherService weatherService, SecurityService securityService,
                             RateLimiterService rateLimiterService) {
        this.weatherService = weatherService;
        this.securityService = securityService;
        this.rateLimiterService = rateLimiterService;

    }
    /**
     * Metodo que Valida si el usuario ha superado el límite de consultas.
     */
    /**
     * Método para validar si el usuario ha superado el límite de consultas y devolver el tiempo de espera restante.
     */
    private ResponseEntity<?> validateRateLimit(String username, String endpoint) {
        if (!rateLimiterService.tryConsume(username, endpoint)) {
            long waitTime = rateLimiterService.getTimeUntilReset(username, endpoint);
            logger.warn("Usuario {} ha excedido el límite de consultas en {}. Debe esperar {} segundos.", username, endpoint, waitTime);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of(
                            "error", "Límite de consultas excedido",
                            "wait_time_seconds", waitTime
                    ));
        }
        return null;
    }
    @GetMapping("/current/{city}") //Devuelve el clima actual de una ciudad.
    @Operation(summary = "Obtener el clima actual", description = "Obtiene el clima actual de una ciudad específica")
    public ResponseEntity<?> getCurrentWeather(@PathVariable String city) {
        String username = securityService.getAuthenticatedUser();
        ResponseEntity<?> rateLimitCheck = validateRateLimit(username, "currentWeather");
        if (rateLimitCheck != null) return rateLimitCheck;

        WeatherDTO response = weatherService.getCurrentWeather(city);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/forecast/{city}") //devuelve pronostico 5 dias para una ciudad
    @Operation(summary = "Obtener pronóstico", description = "Obtiene el pronóstico del clima para los próximos 5 días")
    public ResponseEntity<?> getWeatherForecast(@PathVariable String city) {
        String username = securityService.getAuthenticatedUser();
        ResponseEntity<?> rateLimitCheck = validateRateLimit(username,"forecastWeather");
        if (rateLimitCheck != null) return rateLimitCheck;

        WeatherForecastDTO response = weatherService.getWeatherForecast(city);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/air-quality/{city}") //contaminacion de aire en ciudad
    @Operation(summary = "Obtener contaminacion", description = "Obtiene contaminacion de aire en ciudad")
    public ResponseEntity<?> getAirQuality(@PathVariable String city) {
        String username = securityService.getAuthenticatedUser();
        ResponseEntity<?> rateLimitCheck = validateRateLimit(username, "airQuality");
        if (rateLimitCheck != null) return rateLimitCheck;

        // Llamar al servicio para obtener los datos de calidad del aire
        AirQualityDTO response = weatherService.getAirQuality(city);
        return ResponseEntity.ok(response);
    }
}
