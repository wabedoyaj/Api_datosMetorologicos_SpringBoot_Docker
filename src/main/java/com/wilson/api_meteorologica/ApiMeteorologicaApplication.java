package com.wilson.api_meteorologica;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Meteorológica", version = "1.0", description = "API para obtener datos meteorológicos"))
@EnableCaching // Habilita el sistema de caché en Spring Boot
public class ApiMeteorologicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiMeteorologicaApplication.class, args);
	}

}
