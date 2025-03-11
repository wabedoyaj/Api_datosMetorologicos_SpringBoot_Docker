package com.wilson.api_meteorologica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * Representa un rol de usuario en el sistema.
 * Se almacena en la tabla "roles" de la base de datos.
 */
@Entity
@Table(name = "roles")
@NoArgsConstructor @AllArgsConstructor
@Getter
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Nombre del rol (ejemplo: ROLE_ADMIN, ROLE_USER). */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleName name;

    public UserRole(@NotNull RoleName name) {
        this.name = name;
    }



}
