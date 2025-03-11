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

    /**
     * Configura y gestiona la caché con tiempos de expiración específicos.
     * - "currentWeather": Expira en 10 minutos, hasta 100 elementos.
     * - "forecastWeather": Expira en 1 hora, hasta 50 elementos.
     * - "airQuality": Expira en 30 minutos, hasta 50 elementos.
     * @return CacheManager con la configuración de Caffeine.
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // Configurar caché para clima actual (expira en 10 min)
        cacheManager.registerCustomCache("currentWeather",
                Caffeine.newBuilder()
                        .maximumSize(100) // Máximo 100 elementos en caché
                        .expireAfterWrite(10, TimeUnit.MINUTES) // Expira en 10 minutos
                        .build());
        // Configurar caché para pronóstico del tiempo (expira en 1 hora)
        cacheManager.registerCustomCache("forecastWeather",
                Caffeine.newBuilder()
                        .maximumSize(50)
                        .expireAfterWrite(1, TimeUnit.HOURS) // Expira en 1 hora
                        .build());
        // Configurar caché para calidad del aire (expira en 30 min)
        cacheManager.registerCustomCache("airQuality",
                Caffeine.newBuilder()
                        .maximumSize(50)
                        .expireAfterWrite(30, TimeUnit.MINUTES) // Expira en 30 minutos
                        .build());

        return cacheManager;
    }
}
