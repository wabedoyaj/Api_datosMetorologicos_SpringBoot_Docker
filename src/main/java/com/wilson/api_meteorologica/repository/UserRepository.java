package com.wilson.api_meteorologica.repository;

import com.wilson.api_meteorologica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
   Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
