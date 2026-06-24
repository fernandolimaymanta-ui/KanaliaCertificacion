// src/main/java/com/example/kanaliacertificacion/certificacion/service/dto/ProgresoDiarioRequestDTO.java
package com.example.kanaliacertificacion.certificacion.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProgresoDiarioRequestDTO {

    @NotNull
    @DecimalMin("0.0") @DecimalMax("24.0")
    private Double horasEfectivas;

    @NotNull
    @Min(0) @Max(10)
    private Integer quizPuntaje;

    @NotNull
    private Boolean laboratorioCompletado;

    // "na" | "completado" | "no_completado"
    @NotBlank
    private String simulacroEstado;
}