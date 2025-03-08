package com.wilson.api_meteorologica.service;


import com.wilson.api_meteorologica.entity.User;
import com.wilson.api_meteorologica.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public Optional<User> findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {

        return userRepository.existsByUsername(username);
    }



}
