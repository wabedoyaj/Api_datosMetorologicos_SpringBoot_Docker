package com.wilson.api_meteorologica.service;

import com.wilson.api_meteorologica.config.PrincipalUser;
import com.wilson.api_meteorologica.entity.User;
import com.wilson.api_meteorologica.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio que implementa UserDetailsService para la autenticación en Spring Security.
 * Carga los detalles de un usuario desde la base de datos según su nombre de usuario.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Busca un usuario en la base de datos por su nombre de usuario.
     * Si el usuario no existe, lanza una excepción UsernameNotFoundException.
     * @param username Nombre de usuario ingresado en el login.
     * @return UserDetails con la información del usuario autenticado.
     * @throws UsernameNotFoundException Si el usuario no se encuentra en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        System.out.println("Usuario encontrado: " + user.getUsername());
        System.out.println("Contraseña en BD: " + user.getPassword());
        return PrincipalUser.build(user);
    }
}
