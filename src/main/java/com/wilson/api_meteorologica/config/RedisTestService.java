package com.wilson.api_meteorologica.config;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTestService {

    private final StringRedisTemplate redisTemplate;

    public RedisTestService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void testRedisConnection() {
        try {
            redisTemplate.opsForValue().set("testKey", "testValue");
            String value = redisTemplate.opsForValue().get("testKey");
            System.out.println("✅ Conexión a Redis exitosa. Valor almacenado: " + value);
        } catch (Exception e) {
            System.err.println("❌ Error conectando a Redis: " + e.getMessage());
        }
    }
}

