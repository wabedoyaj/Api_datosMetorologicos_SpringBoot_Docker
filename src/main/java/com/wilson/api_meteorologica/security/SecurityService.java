package com.wilson.api_meteorologica.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
/**
 * Servicio para manejar la seguridad y autenticación de usuarios.
 */
@Service
public class SecurityService {
    public String getAuthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
