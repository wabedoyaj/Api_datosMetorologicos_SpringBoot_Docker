package com.wilson.api_meteorologica.config;

import com.wilson.api_meteorologica.entity.RoleName;
import com.wilson.api_meteorologica.entity.UserRole;
import com.wilson.api_meteorologica.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
/**
 * Clase de configuración que inicializa los roles en la base de datos al iniciar la aplicación.
 * Si un rol no existe, lo crea automáticamente.
 */
@Configuration
public class DataInitializer {

    /**
     * Inicializa los roles en la base de datos al arrancar la aplicación.
     * Si un rol no está registrado, lo inserta.
     * @param roleRepository Repositorio para gestionar los roles en la base de datos.
     * @return Un CommandLineRunner que ejecuta la inserción de roles.
     */
    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            Arrays.stream(RoleName.values()).forEach(roleName -> {
                if (roleRepository.findByName(roleName).isEmpty()) {
                    roleRepository.save(new UserRole(roleName));
                    System.out.println("Rol insertado: " + roleName);
                }
            });
        };
    }
}