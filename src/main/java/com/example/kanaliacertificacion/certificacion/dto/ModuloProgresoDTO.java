// src/main/java/com/example/kanaliacertificacion/certificacion/service/dto/ModuloProgresoDTO.java
package com.example.kanaliacertificacion.certificacion.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ModuloProgresoDTO {

    @NotBlank
    private String moduloId;

    @NotNull @Min(0) @Max(100)
    private Integer porcentaje;
}