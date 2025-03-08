package com.wilson.api_meteorologica.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    @CacheEvict(value = "currentWeather", allEntries = true)
    @DeleteMapping("/clear/currentWeather")
    public String clearCurrentWeatherCache() {
        return "Caché de currentWeather eliminada!";
    }

    @CacheEvict(value = "forecastWeather", allEntries = true)
    @DeleteMapping("/clear/forecastWeather")
    public String clearForecastCache() {
        return "Caché de forecastWeather eliminada!";
    }

    @CacheEvict(value = "airQuality", allEntries = true)
    @DeleteMapping("/clear/airQuality")
    public String clearAirQualityCache() {
        return "Caché de airQuality eliminada!";
    }
}
