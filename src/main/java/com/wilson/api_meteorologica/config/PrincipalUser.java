package com.wilson.api_meteorologica.config;

import com.wilson.api_meteorologica.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.Collection;
/**
 * Representa un usuario autenticado en el sistema para Spring Security.
 * Implementa UserDetails, permitiendo la autenticación y autorización con roles.
 */
public class PrincipalUser implements UserDetails {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor que recibe datos del usuario y sus roles.
     * @param username Nombre de usuario.
     * @param password Contraseña cifrada.
     * @param roles Conjunto de roles asignados al usuario.
     */
    public PrincipalUser(String username, String password, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.authorities = roles.stream().map(role -> (GrantedAuthority) () -> role).collect(Collectors.toSet());
    }

    /**
     * Construye un PrincipalUser a partir de un objeto User.
     * Extrae los roles del usuario y los convierte en GrantedAuthority.
     * @param user Objeto User desde la base de datos.
     * @return Instancia de PrincipalUser con roles asignados.
     */
    public static PrincipalUser build(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        System.out.println("Roles asignados al usuario: " + roles);

        return new PrincipalUser(
                user.getUsername(),
                user.getPassword(),
                roles
        );
    }

    /**
     * Obtiene la lista de roles del usuario.
     * @return Colección de GrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return Contraseña cifrada.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Obtiene el nombre de usuario.
     * @return Nombre de usuario.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indica si la cuenta ha expirado (siempre retorna true, no implementado).
     * @return true (cuenta no expira).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta está bloqueada (siempre retorna true, no implementado).
     * @return true (cuenta no bloqueada).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales han expirado (siempre retorna true, no implementado).
     * @return true (credenciales no expiran).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado (siempre retorna true, no implementado).
     * @return true (usuario habilitado).
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
