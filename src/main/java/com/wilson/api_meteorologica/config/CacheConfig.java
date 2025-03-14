package com.wilson.api_meteorologica.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuración de caché para mejorar el rendimiento de la API.
 * - Usa Caffeine como sistema de caché en memoria.
 * - Define diferentes tiempos de expiración para cada tipo de dato almacenado.
 */
@Configuration
@EnableCaching // Habilita el sistema de caché en Spring Boot
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("currentWeather", "forecastWeather", "airQuality");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES) // Expira después de 10 minutos
                .maximumSize(100) // Máximo de 100 elementos en caché
                .recordStats() // Habilitar métricas
        );
        return cacheManager;
    }
}



