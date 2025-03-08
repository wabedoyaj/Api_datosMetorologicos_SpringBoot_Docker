package com.wilson.api_meteorologica.repository;

import com.wilson.api_meteorologica.entity.AuditConsultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditConsultatiorepository extends JpaRepository<AuditConsultation, Long> {
    List<AuditConsultation> findByUsername(String username);
}
