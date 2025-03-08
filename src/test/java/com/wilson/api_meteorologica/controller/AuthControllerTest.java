package com.wilson.api_meteorologica.controller;


import com.wilson.api_meteorologica.DTO.NewUser;
import com.wilson.api_meteorologica.entity.RoleName;
import com.wilson.api_meteorologica.entity.User;
import com.wilson.api_meteorologica.entity.UserRole;
import com.wilson.api_meteorologica.service.RoleService;
import com.wilson.api_meteorologica.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks //Inyecta mocks en la clase bajo prueba
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    // Prueba registro exitoso
    @Test
    void RegisterUserSuccessfully() {
        // Simular que el usuario NO existe en la base de datos
        when(userService.existsByUsername(anyString())).thenReturn(false);
        // Simular la encriptación de la contraseña
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        // Simular la obtención del rol USER
        when(roleService.getByRolName(RoleName.ROLE_USER)).thenReturn(Optional.of(new UserRole(RoleName.ROLE_USER)));
        // Crear el objeto NewUser de prueba
        NewUser newUser = new NewUser("newUser", "password123");
        // Llamar al controlador para registrar el usuario
        ResponseEntity<?> response = authController.registerUser(newUser, null);
        // Validar que la respuesta sea 201 Created
        assertEquals(201, response.getStatusCode().value());
        assertEquals("Usuario registrado con éxito.", response.getBody());
        // Verificar que se guardó el usuario
        verify(userService, times(1)).saveUser(any(User.class));
    }
    @Test
    void shouldRegisterUserWithAdminRole() {
        // Simular que el usuario NO existe
        when(userService.existsByUsername(anyString())).thenReturn(false);
        // Simular la encriptación de la contraseña
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        // Simular la obtención de roles USER y ADMIN
        when(roleService.getByRolName(RoleName.ROLE_USER)).thenReturn(Optional.of(new UserRole(RoleName.ROLE_USER)));
        when(roleService.getByRolName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(new UserRole(RoleName.ROLE_ADMIN)));
        // Crear el objeto NewUser con rol ADMIN
        NewUser newUser = new NewUser("adminUser", "password123");
        newUser.setRoles(Set.of("admin"));        // Llamar al controlador
        ResponseEntity<?> response = authController.registerUser(newUser, null);
        // Validar que la respuesta sea 201 Created
        assertEquals(201, response.getStatusCode().value());
        assertEquals("Usuario registrado con éxito.", response.getBody());
        // Verificar que se guardó el usuario con rol ADMIN
        verify(userService, times(1)).saveUser(any(User.class));
    }
    @Test
    void RegisterUserExists() {
        // Simular que el usuario  existe en la base de datos
        when(userService.existsByUsername(anyString())).thenReturn(true);
        // Crear el objeto NewUser de prueba
        NewUser newUser = new NewUser("ExistUser", "password123");
        // Llamar al controlador para registrar el usuario
        ResponseEntity<?> response = authController.registerUser(newUser, null);
        // Validar que la respuesta sea 201 Created
        assertEquals(400, response.getStatusCode().value());
        assertEquals("El usuario ya está registrado.", response.getBody());
        // Verificar que NO se guardó el usuario
        verify(userService, never()).saveUser(any(User.class));

    }

}