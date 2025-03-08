package com.wilson.api_meteorologica.controller;

import com.wilson.api_meteorologica.DTO.JwtDto;
import com.wilson.api_meteorologica.DTO.LoginUser;
import com.wilson.api_meteorologica.DTO.NewUser;
import com.wilson.api_meteorologica.config.PrincipalUser;
import com.wilson.api_meteorologica.config.JwtUtils;
import com.wilson.api_meteorologica.entity.RoleName;
import com.wilson.api_meteorologica.entity.User;
import com.wilson.api_meteorologica.entity.UserRole;
import com.wilson.api_meteorologica.repository.UserRepository;
import com.wilson.api_meteorologica.service.RoleService;
import com.wilson.api_meteorologica.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Registro y login de usuarios", description = "Endpoints para registro y login de usuarios")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    @Operation(summary = "Registro de usuario", description = "Registro de un nuevo usuario")
    public ResponseEntity<?> registerUser(@Valid @RequestBody NewUser newUser,BindingResult bindingResult) {
        // Validaciones
        if (userService.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya está registrado.");
        }
        // Crear usuario con contraseña encriptada
        User user = new User(newUser.getUsername(), passwordEncoder.encode(newUser.getPassword()));
        // Asignar roles
        Set<UserRole> roles = new HashSet<>();
        roles.add(roleService.getByRolName(RoleName.ROLE_USER).orElseThrow(()-> new RuntimeException("Role no encontrado")));
        if (newUser.getRoles() != null && newUser.getRoles().contains("admin")) {
            roles.add(roleService.getByRolName(RoleName.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Role no encontrado")));
        }
        user.setRoles(roles);
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito.");
    }

    @PostMapping("/login")
    @Operation(summary = "Login de usuarios registrados ", description = "login de usuarios registrados")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUser loginUser) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        System.out.println("Contraseña ingresada: " + loginUser.getPassword());
        System.out.println("Contraseña en BD: " + user.getPassword());

        // 🚀 Aquí es donde se compara correctamente
        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            System.err.println("⚠ ERROR: Las contraseñas NO coinciden.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
        }

        System.out.println("✔ Contraseñas coinciden, generando token...");

        // Convertir a PrincipalUser para usar authorities
        PrincipalUser principalUser = PrincipalUser.build(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        System.out.println("Token generado: " + jwt);

        return ResponseEntity.ok(new JwtDto(jwt));
    }
}
