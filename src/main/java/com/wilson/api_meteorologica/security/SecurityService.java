package com.wilson.api_meteorologica.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    // Método para obtener el usuario autenticado desde Spring Security
    public String getAuthenticatedUser() { //obtenerUsuarioAutenticado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // ✅ Retorna el username autenticado
        }

        return "anonymous"; //  Si no hay usuario autenticado, usa "anonymous"
    }
}
