package com.wilson.api_meteorologica.repository;

import com.wilson.api_meteorologica.entity.RoleName;
import com.wilson.api_meteorologica.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Integer> {
    Optional<UserRole> findByName(RoleName name);
}