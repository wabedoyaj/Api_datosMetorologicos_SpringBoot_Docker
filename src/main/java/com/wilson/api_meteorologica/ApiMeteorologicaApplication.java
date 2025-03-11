package com.wilson.api_meteorologica;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
/**
 * Clase principal de la API Meteorológica.
 *
 * - @SpringBootApplication: Configura y lanza la aplicación Spring Boot.
 * - @OpenAPIDefinition: Define la documentación de la API con título, versión y descripción en Swagger.
 * - @EnableCaching: Habilita la caché en la aplicación para mejorar el rendimiento de las consultas.
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Meteorológica", version = "1.0", description = "API para obtener datos meteorológicos"))
@EnableCaching
public class ApiMeteorologicaApplication {

	/**
	 * Método principal que inicia la aplicación Spring Boot.
	 * @param args Argumentos de línea de comandos.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApiMeteorologicaApplication.class, args);
	}

}
