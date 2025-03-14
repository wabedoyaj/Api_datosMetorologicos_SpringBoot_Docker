package com.wilson.api_meteorologica.config;


import com.wilson.api_meteorologica.security.JwtAuthFilter;
import com.wilson.api_meteorologica.security.JwtEntryPoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * Configuración de seguridad para la API.
 * - Usa JWT para autenticación sin estado.
 * - Define qué rutas están protegidas y cuáles son de acceso público.
 * - Habilita seguridad en métodos con @PreAuthorize.
 */
@Configuration
@EnableMethodSecurity //  Habilita @PreAuthorize para restringir métodos en los controladores
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final AccessDeniedHandler customAccessDeniedHandler; // Manejador de errores 403
    private final JwtEntryPoint jwtEntryPoint; // Manejador de errores 401

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AccessDeniedHandler customAccessDeniedHandler,
                          JwtEntryPoint jwtEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.jwtEntryPoint = jwtEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/auth/**", "/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception->exception
                        .authenticationEntryPoint(jwtEntryPoint) //manejo 401
                        .accessDeniedHandler(customAccessDeniedHandler) //manejo 403
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
