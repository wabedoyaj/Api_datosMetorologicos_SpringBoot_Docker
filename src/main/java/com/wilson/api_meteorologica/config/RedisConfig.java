package com.wilson.api_meteorologica.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host}") // Inyecta el host desde application.properties
    private String redisHost;

    @Value("${spring.redis.port}") // Inyecta el puerto desde application.properties
    private int redisPort;

    @Value("${spring.redis.timeout}") // Inyecta el timeout desde application.properties
    private long redisTimeout;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        log.info("Conectando a Redis en {}:{} con timeout {}ms", redisHost, redisPort, redisTimeout);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisHost, redisPort);
        factory.setTimeout(redisTimeout);
        return factory;
    }

     /**
     * Configura un RedisTemplate para almacenar y recuperar datos en Redis.
     * Serializa claves y valores como cadenas de texto.
     * @param connectionFactory Fábrica de conexiones a Redis.
     * @return RedisTemplate<String, String> plantilla configurada para Redis.
     */
     @Bean
     public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
         RedisTemplate<String, String> template = new RedisTemplate<>();
         template.setConnectionFactory(connectionFactory);
         template.setKeySerializer(new StringRedisSerializer());
         template.setValueSerializer(new StringRedisSerializer());
         log.info("RedisTemplate configurado correctamente.");
         return template;
     }
}
