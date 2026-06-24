// src/main/java/com/example/kanaliacertificacion/certificacion/repository/AprendizajeRepository.java
package com.example.kanaliacertificacion.certificacion.repository;

import com.example.kanaliacertificacion.certificacion.entity.Aprendizaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AprendizajeRepository extends JpaRepository<Aprendizaje, Long> {

    List<Aprendizaje> findByUsuarioIdAndFechaOrderByIdAsc(Long usuarioId, LocalDate fecha);

    List<Aprendizaje> findByUsuarioIdOrderByFechaDescIdDesc(Long usuarioId);
}