// src/main/java/com/example/kanaliacertificacion/certificacion/entity/ProgresoDiario.java
package com.example.kanaliacertificacion.certificacion.entity;

import com.example.kanaliacertificacion.security.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "progreso_diario",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "fecha"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProgresoDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(nullable = false)
    private LocalDate fecha;

    // Horas efectivas estudiadas (ej: 5.5)
    @Column(nullable = false)
    private Double horasEfectivas;

    // Puntaje quiz: 0-10
    @Column(nullable = false)
    private Integer quizPuntaje;

    // Laboratorio completado: true/false
    @Column(nullable = false)
    private Boolean laboratorioCompletado;

    // Simulacro: "na" | "completado" | "no_completado"
    @Column(nullable = false, length = 20)
    private String simulacroEstado;

    // Score calculado 0-100
    @Column(nullable = false)
    private Integer scoreDiario;

    // Número de semana (1-8) y día (1-56)
    @Column(nullable = false)
    private Integer semana;

    @Column(nullable = false)
    private Integer diaPlan;
}