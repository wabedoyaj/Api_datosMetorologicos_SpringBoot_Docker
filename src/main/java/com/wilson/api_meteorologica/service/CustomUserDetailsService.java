package com.wilson.api_meteorologica.service;

import com.wilson.api_meteorologica.config.PrincipalUser;
import com.wilson.api_meteorologica.entity.User;
import com.wilson.api_meteorologica.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        System.out.println("Usuario encontrado: " + user.getUsername());
        System.out.println("Contraseña en BD: " + user.getPassword());
        return PrincipalUser.build(user);
    }
}
