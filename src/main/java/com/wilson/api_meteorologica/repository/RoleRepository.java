package com.wilson.api_meteorologica.repository;

import com.wilson.api_meteorologica.entity.RoleName;
import com.wilson.api_meteorologica.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repositorio para gestionar los roles de usuario en la base de datos.
 * Extiende JpaRepository para proporcionar operaciones CRUD.
 */
@Repository
public interface RoleRepository extends JpaRepository<UserRole, Integer> {
    /**
     * Busca un rol por su nombre.
     *
     * @param name Nombre del rol (ejemplo: ROLE_ADMIN, ROLE_USER).
     * @return Un Optional que contiene el rol si existe.
     */
    Optional<UserRole> findByName(RoleName name);
}