package com.wilson.api_meteorologica.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
/**
 * DTO para el inicio de sesión de usuarios.
 * Captura el nombre de usuario y la contraseña enviados en la solicitud de login.
 * Se usa en la autenticación para validar credenciales y generar un token JWT.
 */
@Getter @Setter
public class LoginUser {
    @NotBlank
    private String username;

    @NotBlank
    private String password;


}
