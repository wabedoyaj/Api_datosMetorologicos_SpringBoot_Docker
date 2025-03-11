package com.wilson.api_meteorologica.controller;

import com.wilson.api_meteorologica.entity.AuditConsultation;
import com.wilson.api_meteorologica.repository.AuditConsultatiorepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Administra auditorías sobre las consultas realizadas en la API
 */
@RestController
@RequestMapping("/api/audit")
@Tag(name = "Auditoria", description = "Endpoints para listado de usuarios")
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación en todo el controlador
public class AuditController {
    @Autowired
    private AuditConsultatiorepository consultatiorepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/Alls")
    @Operation(summary = "Obtener todo el listado de consultas", description = "Obtiene todas las consultas realizadas por todos los usuarios en la API")
    public List<AuditConsultation> getAllQuery(){
        return consultatiorepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{username}")
    @Operation(summary = "Obtener listado de consultas por usuario", description = "Obtiene consultas rtealizadas por el usuarios en la API")
    public List<AuditConsultation> getQueryByUsername(@PathVariable String username){
        return consultatiorepository.findByUsername(username);
    }
}
