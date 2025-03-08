package com.wilson.api_meteorologica.service;

import com.wilson.api_meteorologica.entity.RoleName;
import com.wilson.api_meteorologica.entity.UserRole;
import com.wilson.api_meteorologica.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<UserRole> getByRolName(RoleName rolName) {
        return roleRepository.findByName(rolName);
    }

    public void save(UserRole role){
        roleRepository.save(role);

    }
}