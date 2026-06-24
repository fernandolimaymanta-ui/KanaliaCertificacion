// src/main/java/com/example/kanaliacertificacion/certificacion/repository/ModuloProgresoRepository.java
package com.example.kanaliacertificacion.certificacion.repository;

import com.example.kanaliacertificacion.certificacion.entity.ModuloProgreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ModuloProgresoRepository extends JpaRepository<ModuloProgreso, Long> {

    List<ModuloProgreso> findByUsuarioId(Long usuarioId);

    Optional<ModuloProgreso> findByUsuarioIdAndModuloId(Long usuarioId, String moduloId);
}