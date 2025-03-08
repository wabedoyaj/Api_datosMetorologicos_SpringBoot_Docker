package com.wilson.api_meteorologica.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final int MAX_REQUESTS = 3; // Máximo 100 solicitudes por usuario
    private static final long TIME_WINDOW = 2; // 1 hora

    public RateLimiterService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean tryConsume(String userId) {
        String key = "rate-limit:" + userId;

        // Incrementar el contador en Redis
        Long requestCount = redisTemplate.opsForValue().increment(key);

        // Si es la primera solicitud, establecer tiempo de expiración (1 hora)
        if (requestCount != null && requestCount == 1) {
            redisTemplate.expire(key, TIME_WINDOW, TimeUnit.MINUTES);
        }

        // Si supera el límite, denegar acceso
        return requestCount != null && requestCount <= MAX_REQUESTS;
    }
}