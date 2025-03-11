package com.wilson.api_meteorologica.service;

/**
 * Servicio para la gestión de usuarios en la aplicación.
 * Proporciona métodos para registrar, buscar y validar usuarios en la base de datos.
 */
import com.wilson.api_meteorologica.entity.User;
import com.wilson.api_meteorologica.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Guarda un nuevo usuario en la base de datos.
     * @param user Objeto User con los datos del usuario.
     * @return El usuario guardado.
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username Nombre de usuario a buscar.
     * @return Un Optional con el usuario si existe, o vacío si no se encuentra.
     */
    public Optional<User> findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    /**
     * Verifica si un usuario ya existe en la base de datos.
     * @param username Nombre de usuario a verificar.
     * @return true si el usuario existe, false si no.
     */
    public boolean existsByUsername(String username) {

        return userRepository.existsByUsername(username);
    }

}
