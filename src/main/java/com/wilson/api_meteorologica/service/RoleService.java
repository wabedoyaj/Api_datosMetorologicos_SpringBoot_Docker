package com.wilson.api_meteorologica.service;

import com.wilson.api_meteorologica.entity.RoleName;
import com.wilson.api_meteorologica.entity.UserRole;
import com.wilson.api_meteorologica.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
/**
 * Servicio para la gestión de roles de usuario en la aplicación.
 * Permite obtener roles por su nombre y guardarlos en la base de datos.
 */
@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Obtiene un rol por su nombre.
     * @param rolName Nombre del rol (ejemplo: ROLE_ADMIN, ROLE_USER)
     * @return Un Optional<UserRole> con el rol encontrado o vacío si no existe.
     */
    public Optional<UserRole> getByRolName(RoleName rolName) {
        return roleRepository.findByName(rolName);
    }

    /**
     * Guarda un nuevo rol en la base de datos.
     * @param role Objeto UserRole a guardar.
     */
    public void save(UserRole role){
        roleRepository.save(role);

    }
}