package com.wilson.api_meteorologica.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching // Habilita el sistema de caché en Spring Boot
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // Configurar diferentes tiempos de expiración para cada tipo de caché
        cacheManager.registerCustomCache("currentWeather",
                Caffeine.newBuilder()
                        .maximumSize(100) // Máximo 100 elementos en caché
                        .expireAfterWrite(10, TimeUnit.MINUTES) // Expira en 10 minutos
                        .build());

        cacheManager.registerCustomCache("forecastWeather",
                Caffeine.newBuilder()
                        .maximumSize(50)
                        .expireAfterWrite(1, TimeUnit.HOURS) // Expira en 1 hora
                        .build());

        cacheManager.registerCustomCache("airQuality",
                Caffeine.newBuilder()
                        .maximumSize(50)
                        .expireAfterWrite(30, TimeUnit.MINUTES) // Expira en 30 minutos
                        .build());

        return cacheManager;
    }
}
