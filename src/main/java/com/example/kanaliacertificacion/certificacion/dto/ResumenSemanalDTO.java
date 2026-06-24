// src/main/java/com/example/kanaliacertificacion/certificacion/service/dto/ResumenSemanalDTO.java
package com.example.kanaliacertificacion.certificacion.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumenSemanalDTO {
    private Integer semana;
    private List<ProgresoDiarioResponseDTO> dias;   // 7 elementos (null si no hay registro)
    private Double promedioScore;
    private String estado; // "Excelente" | "Aceptable" | "En riesgo" | "Pendiente"
}