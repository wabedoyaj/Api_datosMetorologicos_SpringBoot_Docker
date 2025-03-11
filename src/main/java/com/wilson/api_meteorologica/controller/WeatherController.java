package com.wilson.api_meteorologica.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wilson.api_meteorologica.DTO.AirQualityDTO;
import com.wilson.api_meteorologica.DTO.ErrorResponseDTO;
import com.wilson.api_meteorologica.DTO.WeatherDTO;
import com.wilson.api_meteorologica.DTO.WeatherForecastDTO;
import com.wilson.api_meteorologica.responApi.AirQualityResponse;
import com.wilson.api_meteorologica.responApi.WeatherResponse;
import com.wilson.api_meteorologica.security.SecurityService;
import com.wilson.api_meteorologica.service.RateLimiterService;
import com.wilson.api_meteorologica.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Proporciona información del clima y calidad del aire.
 */
@RestController
@RequestMapping("/api/weather")
@Tag(name = "Clima", description = "Endpoints relacionados con el clima")
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación en todo el controlador
public class WeatherController {

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private ObjectMapper objectMapper;

    private final RateLimiterService rateLimiterService;

    public WeatherController(RateLimiterService rateLimiterService) {
        System.out.println("Injected RateLimiterService instance: " + rateLimiterService);
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/current/{city}") //Devuelve el clima actual de una ciudad.
    @Operation(summary = "Obtener el clima actual", description = "Obtiene el clima actual de una ciudad específica")
    public ResponseEntity<?> getCurrentWeather(@PathVariable String city) {
        String username = securityService.getAuthenticatedUser();
        if(!rateLimiterService.tryConsume(username)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ErrorResponseDTO());
        }
        WeatherDTO response = weatherService.getCurrentWeather(city);
        weatherService.registerQuery(username, "currentWeather", city, response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/forecast/{city}") //devuelve pronostico 5 dias para una ciudad
    @Operation(summary = "Obtener pronóstico", description = "Obtiene el pronóstico del clima para los próximos 5 días")
    public ResponseEntity<?> getWeatherForecast(@PathVariable String city) {
        String username = securityService.getAuthenticatedUser();
        if(!rateLimiterService.tryConsume(username)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ErrorResponseDTO());
        }
        WeatherForecastDTO response = weatherService.getWeatherForecast(city);
        weatherService.registerQuery(username, "forecastWeather", city, response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/air-quality/{city}") //contaminacion de aire en ciudad
    @Operation(summary = "Obtener contaminacion", description = "Obtiene contaminacion de aire en ciudad")
    public ResponseEntity<?> getAirQuality(@PathVariable String city) {
        String username = securityService.getAuthenticatedUser();
        if(!rateLimiterService.tryConsume(username)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ErrorResponseDTO());
        }
        // Llamar al servicio para obtener los datos de calidad del aire
        AirQualityDTO response = weatherService.getAirQuality(city);
        weatherService.registerQuery(username, "air-qualityWeather", city, response);
        return ResponseEntity.ok(response);
    }
}
