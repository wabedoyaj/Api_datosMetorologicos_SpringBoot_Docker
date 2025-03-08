package com.wilson.api_meteorologica.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_consultation")
@Getter @Setter
@NoArgsConstructor
public class AuditConsultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String queryType;
    private String city;
    private LocalDateTime dateQuery;

    @Lob  // Permite almacenar respuestas largas
    @Column(columnDefinition = "TEXT")
    private String responseApi;

    public AuditConsultation(String username, String queryType, String city,
                             LocalDateTime dateQuery, String responseApi) {
        this.username = username;
        this.queryType = queryType;
        this.city = city;
        this.dateQuery = dateQuery;
        this.responseApi = responseApi;
    }
}
