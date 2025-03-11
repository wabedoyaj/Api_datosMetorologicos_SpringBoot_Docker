package com.wilson.api_meteorologica.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * DTO para el registro de nuevos usuarios en la API.
 * Contiene la información necesaria para crear un usuario, incluyendo nombre, contraseña y roles opcionales.
 */
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class NewUser {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    /** Roles asignados al usuario (opcional, por defecto vacío). */
    private Set<String> roles = new HashSet<>();

    /**
     * Constructor con parámetros básicos (sin roles).
     * @param username Nombre de usuario.
     * @param password Contraseña del usuario.
     */
    public NewUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
