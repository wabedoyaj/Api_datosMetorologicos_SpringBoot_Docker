package com.wilson.api_meteorologica.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
/**
 * Servicio para manejar la seguridad y autenticación de usuarios.
 */
@Service
public class SecurityService {
    /**
     * Obtiene el nombre de usuario del usuario autenticado en la sesión actual.
     *
     * @return Nombre de usuario autenticado, o "anonymous" si no hay usuario autenticado.
     */
    public String getAuthenticatedUser() { //obtenerUsuarioAutenticado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // Retorna el username autenticado
        }

        return "anonymous"; //  Si no hay usuario autenticado, usa "anonymous"
    }
}
