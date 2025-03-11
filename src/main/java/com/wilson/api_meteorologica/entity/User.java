package com.wilson.api_meteorologica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
/**
 * Representa la entidad de usuario en la base de datos.
 * Contiene la información del usuario, como su nombre de usuario, contraseña y roles.
 */
@Entity
@Table(name = "users")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    /**
     * Roles asignados al usuario. Relación **muchos a muchos** con la entidad `UserRole`.
     * Se usa `FetchType.EAGER` para cargar los roles junto con el usuario.
     */
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    /**
     * Constructor para crear un usuario con nombre de usuario y contraseña.
     * @param username Nombre de usuario
     * @param password Contraseña cifrada
     */
    private Set<UserRole> roles = new HashSet<>();
    public User(@NotNull String username, @NotNull String password) {
        this.username = username;
        this.password = password;
    }


}
