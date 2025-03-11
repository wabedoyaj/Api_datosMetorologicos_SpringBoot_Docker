package com.wilson.api_meteorologica.config;

import com.wilson.api_meteorologica.entity.RoleName;
import com.wilson.api_meteorologica.entity.UserRole;
import com.wilson.api_meteorologica.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            Arrays.stream(RoleName.values()).forEach(roleName -> {
                if (roleRepository.findByName(roleName).isEmpty()) {
                    roleRepository.save(new UserRole(roleName));
                    System.out.println("✅ Rol insertado: " + roleName);
                }
            });
        };
    }
}