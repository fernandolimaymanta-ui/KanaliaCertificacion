// src/main/java/com/example/kanaliacertificacion/certificacion/repository/BloqueoRepository.java
package com.example.kanaliacertificacion.certificacion.repository;

import com.example.kanaliacertificacion.certificacion.entity.Bloqueo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BloqueoRepository extends JpaRepository<Bloqueo, Long> {

    List<Bloqueo> findByUsuarioIdAndFechaOrderByIdAsc(Long usuarioId, LocalDate fecha);

    List<Bloqueo> findByUsuarioIdOrderByFechaDescIdDesc(Long usuarioId);
}