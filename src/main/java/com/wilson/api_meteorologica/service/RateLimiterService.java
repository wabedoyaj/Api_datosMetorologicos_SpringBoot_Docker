package com.wilson.api_meteorologica.service;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * Servicio para limitar la cantidad de solicitudes por usuario en un período de tiempo.
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
     * Verifica si un usuario puede realizar una nueva solicitud.
     * @param userId  ID del usuario.
     * @param endpoint  Nombre del endpoint para rate-limiting individual.
     * @return `true` si la solicitud está permitida, `false` si ha superado el límite.
     */
    public boolean tryConsume(String userId, String endpoint) {
        String key = "rate-limit:" + userId + ":" + endpoint; // Clave única para usuario + endpoint

        List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) {
                operations.multi(); // Inicia transacción en Redis
                Long requestCount = operations.opsForValue().increment(key);

                if (requestCount != null && requestCount == 1) {
                    operations.expire(key, TIME_WINDOW, TimeUnit.HOURS);
                }
                return operations.exec(); // Ejecuta la transacción
            }
        });

        Long requestCount = results != null && !results.isEmpty() ? (Long) results.get(0) : 0;
        return requestCount != null && requestCount <= MAX_REQUESTS;
    }

    /**
     * Obtiene el tiempo restante antes de que el usuario pueda hacer nuevas solicitudes.
     * @param userId  ID del usuario.
     * @param endpoint  Endpoint específico.
     * @return Tiempo restante en segundos o 0 si ya puede hacer solicitudes.
     */
    public long getTimeUntilReset(String userId, String endpoint) {
        String key = "rate-limit:" + userId + ":" + endpoint;
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return (ttl != null && ttl > 0) ? ttl : 0;
    }
}
