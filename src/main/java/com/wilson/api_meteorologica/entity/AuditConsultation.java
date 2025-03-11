package com.wilson.api_meteorologica.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
/**
 * Entidad que representa el historial de consultas realizadas a la API.
 * Registra información sobre el usuario, tipo de consulta, ciudad y la respuesta obtenida.
 */
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

    /**
     * Constructor para inicializar una consulta de auditoría.
     *
     * @param username   Usuario que realizó la consulta
     * @param queryType  Tipo de consulta realizada
     * @param city       Ciudad consultada
     * @param dateQuery  Fecha y hora de la consulta
     * @param responseApi Respuesta obtenida de la API
     */
    public AuditConsultation(String username, String queryType, String city,
                             LocalDateTime dateQuery, String responseApi) {
        this.username = username;
        this.queryType = queryType;
        this.city = city;
        this.dateQuery = dateQuery;
        this.responseApi = responseApi;
    }
}
