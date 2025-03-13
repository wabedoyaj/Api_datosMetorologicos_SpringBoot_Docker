package com.wilson.api_meteorologica.security;

import com.wilson.api_meteorologica.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/**
 * Filtro de autenticación JWT que se ejecuta en cada solicitud.
 * Verifica la presencia y validez de un token JWT en el encabezado "Authorization".
 * Si el token es válido, autentica al usuario en el contexto de seguridad de Spring.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Intercepta cada solicitud para verificar la autenticación con JWT.
     * @param request  Solicitud HTTP entrante.
     * @param response Respuesta HTTP.
     * @param filterChain Cadena de filtros de seguridad.
     * @throws ServletException Si ocurre un error en el filtro.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Obtiene el encabezado de autorización
        String header = request.getHeader("Authorization");

        // Verifica si el encabezado es nulo o no comienza con "Bearer "
        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("No se encontró un token en el encabezado.");
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae el token sin el prefijo "Bearer "
        String token = header.substring(7);
        System.out.println("Token recibido: " + token);

        try {
            // Extrae el nombre de usuario desde el token JWT
            String username = jwtUtils.getUsernameFromJwtToken(token);

            // Si el usuario no está autenticado, procede con la validación del token
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // Verifica la validez del token
                if (jwtUtils.validateJwtToken(token)) {
                    // Crea la autenticación del usuario y la almacena en el contexto de seguridad
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    System.out.println("Usuario autenticado correctamente: " + username);
                } else {
                    System.err.println("Token JWT no válido.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al procesar JWT: " + e.getMessage());
        }
        // Continúa con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
