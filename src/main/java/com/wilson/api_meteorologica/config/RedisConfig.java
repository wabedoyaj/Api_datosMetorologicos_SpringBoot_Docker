package com.wilson.api_meteorologica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
/**
 * Configuración de Redis para la API.
 * Permite la conexión y el uso de Redis como caché de datos.
 */
@Configuration
public class RedisConfig {

    /**
     * Crea y configura la conexión a Redis.
     * Especifica el host, puerto y tiempo de espera.
     * @return RedisConnectionFactory conexión a Redis.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Especifica el host y puerto para el contenedor Redis
        LettuceConnectionFactory factory = new LettuceConnectionFactory("redis", 6379);
        factory.setTimeout(60000); // Establece el tiempo de espera de la conexión
        return factory;
    }

    /**
     * Configura un RedisTemplate para almacenar y recuperar datos en Redis.
     * Serializa claves y valores como cadenas de texto.
     * @param connectionFactory Fábrica de conexiones a Redis.
     * @return RedisTemplate<String, String> plantilla configurada para Redis.
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(
            RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
