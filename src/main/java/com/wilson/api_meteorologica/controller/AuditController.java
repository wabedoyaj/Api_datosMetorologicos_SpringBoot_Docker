package com.wilson.api_meteorologica.controller;

import com.wilson.api_meteorologica.entity.AuditConsultation;
import com.wilson.api_meteorologica.repository.AuditConsultatiorepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final AuditConsultatiorepository consultatiorepository;

    public AuditController(AuditConsultatiorepository consultatiorepository) {
        this.consultatiorepository = consultatiorepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/Alls")
    @Operation(summary = "Obtener todo el listado de consultas", description = "Obtiene todas las consultas realizadas por todos los usuarios en la API")
    public ResponseEntity<List<AuditConsultation>> getAllQuery(){
        List<AuditConsultation> consults = consultatiorepository.findAll();
        return ResponseEntity.ok(consults);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{username}")
    @Operation(summary = "Obtener listado de consultas por usuario", description = "Obtiene consultas rtealizadas por el usuarios en la API")
    public ResponseEntity<?> getQueryByUsername(@PathVariable String username) {
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre de usuario no puede estar vacío");
        }
        List<AuditConsultation> consults = consultatiorepository.findByUsername(username);
        if (consults.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontraron consultas para el usuario: " + username);
        }

        return ResponseEntity.ok(consults);
    }
}
