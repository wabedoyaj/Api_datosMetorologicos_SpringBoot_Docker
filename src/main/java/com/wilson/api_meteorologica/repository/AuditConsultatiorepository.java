package com.wilson.api_meteorologica.repository;

import com.wilson.api_meteorologica.entity.AuditConsultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * Repositorio para gestionar las consultas de auditoría en la base de datos.
 * Extiende JpaRepository para operaciones CRUD sobre la entidad AuditConsultation.
 */
public interface AuditConsultatiorepository extends JpaRepository<AuditConsultation, Long> {

    /**
     * Busca todas las consultas realizadas por un usuario específico.
     *
     * @param username Nombre de usuario.
     * @return Lista de registros de auditoría asociados al usuario.
     */
    List<AuditConsultation> findByUsername(String username);
}
