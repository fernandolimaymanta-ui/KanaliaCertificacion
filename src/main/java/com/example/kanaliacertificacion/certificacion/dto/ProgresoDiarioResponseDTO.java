// src/main/java/com/example/kanaliacertificacion/certificacion/service/dto/ProgresoDiarioResponseDTO.java
package com.example.kanaliacertificacion.certificacion.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProgresoDiarioResponseDTO {
    private Long id;
    private LocalDate fecha;
    private Double horasEfectivas;
    private Integer quizPuntaje;
    private Boolean laboratorioCompletado;
    private String simulacroEstado;
    private Integer scoreDiario;
    private Integer semana;
    private Integer diaPlan;
}