package com.wilson.api_meteorologica.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
/**
 * Servicio para limitar la cantidad de solicitudes por usuario en un período de tiempo.
 * Utiliza Redis para almacenar y rastrear el número de solicitudes realizadas por cada usuario.
 */
@Service
public class RateLimiterService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final int MAX_REQUESTS = 100; // Máximo 100 solicitudes por usuario
    private static final long TIME_WINDOW = 1; // 1 hora

    /**
     * Constructor que inyecta RedisTemplate para manejar el almacenamiento de los límites de tasa.
     * @param redisTemplate Instancia de RedisTemplate para interactuar con Redis.
     */
    public RateLimiterService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Verifica si un usuario puede realizar una nueva solicitud según su historial en Redis.
     * @param userId ID del usuario que realiza la solicitud.
     * @return true si la solicitud está permitida, false si ha superado el límite.
     */
    public boolean tryConsume(String userId) {
        String key = "rate-limit:" + userId;

        // Incrementar el contador en Redis para rastrear el número de solicitudes del usuario
        Long requestCount = redisTemplate.opsForValue().increment(key);

        // Si es la primera solicitud, establecer tiempo de expiración (1 hora)
        if (requestCount != null && requestCount == 1) {
            redisTemplate.expire(key, TIME_WINDOW, TimeUnit.HOURS);
        }

        // Retorna true si el usuario no ha superado el límite de solicitudes
        return requestCount != null && requestCount <= MAX_REQUESTS;
    }
}