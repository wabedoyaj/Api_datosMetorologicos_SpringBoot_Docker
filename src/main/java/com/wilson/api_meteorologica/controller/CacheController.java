package com.wilson.api_meteorologica.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Borra caché de consultas meteorológicas
 */
@RestController
@RequestMapping("/api/cache")
@Tag(name = "Cache", description = "Endpoints para borrado de cache")
@SecurityRequirement(name = "bearerAuth")
public class CacheController {

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "currentWeather", allEntries = true)
    @DeleteMapping("/clear/currentWeather")
    public String clearCurrentWeatherCache() {
        return "Caché de currentWeather eliminada!";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "forecastWeather", allEntries = true)
    @DeleteMapping("/clear/forecastWeather")
    public String clearForecastCache() {
        return "Caché de forecastWeather eliminada!";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "airQuality", allEntries = true)
    @DeleteMapping("/clear/airQuality")
    public String clearAirQualityCache() {
        return "Caché de airQuality eliminada!";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = {"currentWeather", "forecastWeather", "airQuality"}, allEntries = true)
    @DeleteMapping("/clear/all")
    public String clearAllCaches() {
        return "Todas las cachés han sido eliminadas!";
    }
}
