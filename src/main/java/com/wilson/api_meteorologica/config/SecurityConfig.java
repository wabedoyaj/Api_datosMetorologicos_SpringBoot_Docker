package com.wilson.api_meteorologica.config;


import com.wilson.api_meteorologica.security.JwtAuthFilter;
import com.wilson.api_meteorologica.security.JwtEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
    @Autowired
    @Lazy // Evita ciclos de dependencias en la inyección de JwtAuthFilter
    private JwtAuthFilter jwtAuthFilter;

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     * - Deshabilita CSRF, ya que las APIs suelen manejar seguridad con tokens.
     * - Establece autenticación sin estado (STATELESS) para no usar sesiones.
     * - Permite acceso público a Swagger, Actuator y autenticación.
     * - Requiere autenticación en todos los demás endpoints.
     * - Agrega el filtro de autenticación JWT antes del filtro de usuario y contraseña.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // Deshabilita protección CSRF para peticiones REST
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No usa sesión
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/auth/**").permitAll() // Permitir endpoints de autenticación sin autenticación
                        .requestMatchers("/actuator/health").permitAll() // Permite acceso libre a Actuator
                        .anyRequest().authenticated() // Proteger el resto de endpoints
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new JwtEntryPoint()))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Agregar filtro JWT
                .build();
    }
    /**
     * Proporciona el manejador de autenticación de Spring Security.
     * Se encarga de autenticar a los usuarios mediante credenciales válidas.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Proporciona un codificador de contraseñas seguro usando BCrypt.
     * Se utiliza para cifrar contraseñas antes de almacenarlas en la base de datos
     * y para verificar contraseñas durante la autenticación.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
