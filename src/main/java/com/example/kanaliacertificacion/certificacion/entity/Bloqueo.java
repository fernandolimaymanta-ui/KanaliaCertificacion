// src/main/java/com/example/kanaliacertificacion/certificacion/entity/Bloqueo.java
package com.example.kanaliacertificacion.certificacion.entity;

import com.example.kanaliacertificacion.security.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "bloqueos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bloqueo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 1000)
    private String texto;
}