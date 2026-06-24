// src/main/java/com/example/kanaliacertificacion/certificacion/service/dto/DashboardResumenDTO.java
package com.example.kanaliacertificacion.certificacion.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardResumenDTO {
    // Datos del día actual
    private ProgresoDiarioResponseDTO hoy;

    // Semana actual (1-8)
    private Integer semanaActual;

    // Día del plan actual (1-56)
    private Integer diaActual;

    // Progreso global (días registrados / 56)
    private Integer diasRegistrados;
    private Integer progresoGlobalPct;

    // Historial de las 8 semanas
    private List<ResumenSemanalDTO> historial;

    // Módulos
    private Map<String, Integer> modulos;
}