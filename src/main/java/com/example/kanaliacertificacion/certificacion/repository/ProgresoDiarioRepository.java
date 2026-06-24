// src/main/java/com/example/kanaliacertificacion/certificacion/repository/ProgresoDiarioRepository.java
package com.example.kanaliacertificacion.certificacion.repository;

import com.example.kanaliacertificacion.certificacion.entity.ProgresoDiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgresoDiarioRepository extends JpaRepository<ProgresoDiario, Long> {

    Optional<ProgresoDiario> findByUsuarioIdAndFecha(Long usuarioId, LocalDate fecha);

    List<ProgresoDiario> findByUsuarioIdOrderByFechaAsc(Long usuarioId);

    List<ProgresoDiario> findByUsuarioIdAndSemanaOrderByFechaAsc(Long usuarioId, Integer semana);

    boolean existsByUsuarioIdAndFecha(Long usuarioId, LocalDate fecha);

    // Promedio de score por semana
    @Query("SELECT AVG(p.scoreDiario) FROM ProgresoDiario p WHERE p.usuario.id = :usuarioId AND p.semana = :semana")
    Optional<Double> promedioScorePorSemana(Long usuarioId, Integer semana);

    // Total de días registrados
    @Query("SELECT COUNT(p) FROM ProgresoDiario p WHERE p.usuario.id = :usuarioId")
    Long totalDiasRegistrados(Long usuarioId);
}