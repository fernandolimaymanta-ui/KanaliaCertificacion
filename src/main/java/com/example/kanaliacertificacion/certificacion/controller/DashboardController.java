// src/main/java/com/example/kanaliacertificacion/certificacion/controller/DashboardController.java
package com.example.kanaliacertificacion.certificacion.controller;

import com.example.kanaliacertificacion.certificacion.service.ReporteService;
import com.example.kanaliacertificacion.certificacion.dto.*;
import com.example.kanaliacertificacion.security.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ReporteService reporteService;
    private final JwtTokenUtil   jwtTokenUtil;

    // ─── Helper: extrae username del header Authorization ───────────────────

    private String getUsername(String authHeader) {
        // authHeader = "Bearer eyJhbGc..."
        String token = authHeader.substring(7); // quita "Bearer "
        return jwtTokenUtil.extractUsername(token);
    }

    // ─── Dashboard Completo ─────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<DashboardResumenDTO> getDashboard(
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(
                reporteService.obtenerDashboard(getUsername(auth)));
    }

    // ─── Progreso Diario ────────────────────────────────────────────────────

    @PostMapping("/progreso")
    public ResponseEntity<ProgresoDiarioResponseDTO> guardarProgreso(
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody ProgresoDiarioRequestDTO req) {
        return ResponseEntity.ok(
                reporteService.guardarOActualizarProgreso(getUsername(auth), req));
    }

    @GetMapping("/progreso/hoy")
    public ResponseEntity<ProgresoDiarioResponseDTO> getProgresoHoy(
            @RequestHeader("Authorization") String auth) {
        return reporteService.obtenerProgresoHoy(getUsername(auth))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/progreso/historial")
    public ResponseEntity<List<ProgresoDiarioResponseDTO>> getHistorial(
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(
                reporteService.obtenerTodoElProgreso(getUsername(auth)));
    }

    // ─── Módulos ────────────────────────────────────────────────────────────

    @GetMapping("/modulos")
    public ResponseEntity<List<ModuloProgresoDTO>> getModulos(
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(
                reporteService.obtenerModulos(getUsername(auth)));
    }

    @PutMapping("/modulos")
    public ResponseEntity<ModuloProgresoDTO> actualizarModulo(
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody ModuloProgresoDTO dto) {
        return ResponseEntity.ok(
                reporteService.actualizarModulo(getUsername(auth), dto));
    }

    // ─── Aprendizajes ───────────────────────────────────────────────────────

    @GetMapping("/aprendizajes/hoy")
    public ResponseEntity<List<NotaDTO>> getAprendizajesHoy(
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(
                reporteService.obtenerAprendizajesHoy(getUsername(auth)));
    }

    @PostMapping("/aprendizajes")
    public ResponseEntity<NotaDTO> agregarAprendizaje(
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody NotaDTO dto) {
        return ResponseEntity.ok(
                reporteService.agregarAprendizaje(getUsername(auth), dto));
    }

    // ─── Bloqueos ───────────────────────────────────────────────────────────

    @GetMapping("/bloqueos/hoy")
    public ResponseEntity<List<NotaDTO>> getBloqueosHoy(
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(
                reporteService.obtenerBloqueosHoy(getUsername(auth)));
    }

    @PostMapping("/bloqueos")
    public ResponseEntity<NotaDTO> agregarBloqueo(
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody NotaDTO dto) {
        return ResponseEntity.ok(
                reporteService.agregarBloqueo(getUsername(auth), dto));
    }
}