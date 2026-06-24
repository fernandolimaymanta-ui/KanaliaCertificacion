// src/main/java/com/example/kanaliacertificacion/certificacion/entity/ModuloProgreso.java
package com.example.kanaliacertificacion.certificacion.entity;

import com.example.kanaliacertificacion.security.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "modulo_progreso",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "modulo_id"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ModuloProgreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    // Identificador del módulo: "flow","sec","apis","admin","scale","ux","apps","dist","tools"
    @Column(name = "modulo_id", nullable = false, length = 20)
    private String moduloId;

    // Porcentaje avance 0-100
    @Column(nullable = false)
    private Integer porcentaje;
}