// src/main/java/com/example/kanaliacertificacion/certificacion/service/dto/NotaDTO.java
package com.example.kanaliacertificacion.certificacion.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotaDTO {
    private Long id;
    private LocalDate fecha;

    @NotBlank @Size(max = 1000)
    private String texto;
}