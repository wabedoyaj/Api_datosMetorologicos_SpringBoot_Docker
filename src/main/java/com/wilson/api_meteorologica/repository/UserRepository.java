package com.wilson.api_meteorologica.repository;
/**
 * Repositorio para gestionar operaciones en la base de datos relacionadas con la entidad User.
 * Extiende JpaRepository para proporcionar métodos CRUD automáticos.
 */
import com.wilson.api_meteorologica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    /**
     * Busca un usuario por su nombre de usuario.
     * @param username Nombre de usuario a buscar.
     * @return Un Optional<User> que contiene el usuario si existe.
     */
   Optional<User> findByUsername(String username);

    /**
     * Verifica si un usuario con el nombre de usuario dado existe en la base de datos.
     * @param username Nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    boolean existsByUsername(String username);
}
