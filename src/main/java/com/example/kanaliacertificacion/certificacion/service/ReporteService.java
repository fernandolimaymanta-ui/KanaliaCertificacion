// src/main/java/com/example/kanaliacertificacion/certificacion/service/ReporteService.java
package com.example.kanaliacertificacion.certificacion.service;

import com.example.kanaliacertificacion.certificacion.entity.*;
import com.example.kanaliacertificacion.certificacion.repository.*;
import com.example.kanaliacertificacion.certificacion.dto.*;
import com.example.kanaliacertificacion.security.User;
import com.example.kanaliacertificacion.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private static final LocalDate FECHA_INICIO = LocalDate.of(2026, 6, 9);

    private final ProgresoDiarioRepository progresoDiarioRepo;
    private final AprendizajeRepository    aprendizajeRepo;
    private final BloqueoRepository        bloqueoRepo;
    private final ModuloProgresoRepository moduloProgresoRepo;
    private final UserRepository           userRepo;

    // ─── Helper: obtener User por username ──────────────────────────────────

    private User getUsuario(String username) {
        User u = userRepo.findByUsername(username);

        // Validamos manualmente que no sea null
        if (u == null) {
            throw new RuntimeException("Usuario no encontrado: " + username);
        }

        return u;
    }

    // ─── Cálculos internos ──────────────────────────────────────────────────

    private int calcularScore(Double horas, Integer quiz, Boolean lab, String simulacro) {
        double horasPct = Math.min(horas / 6.0, 1.0) * 100;
        double quizPct  = (quiz / 10.0) * 100;
        double labPct   = lab ? 100.0 : 0.0;

        if ("na".equals(simulacro)) {
            return (int) Math.round(horasPct * 0.25 + quizPct * 0.25 + labPct * 0.50);
        }
        double simPct = "completado".equals(simulacro) ? 100.0 : 0.0;
        return (int) Math.round(horasPct * 0.20 + quizPct * 0.20 + labPct * 0.40 + simPct * 0.20);
    }

    private int calcularSemana(LocalDate fecha) {
        long dias = ChronoUnit.DAYS.between(FECHA_INICIO, fecha);
        return (int) Math.min(8, (dias / 7) + 1);
    }

    private int calcularDiaPlan(LocalDate fecha) {
        long dias = ChronoUnit.DAYS.between(FECHA_INICIO, fecha);
        return (int) Math.max(1, dias + 1);
    }

    private String estadoScore(Double promedio) {
        if (promedio == null) return "Pendiente";
        if (promedio >= 85)   return "Excelente";
        if (promedio >= 70)   return "Aceptable";
        return "En riesgo";
    }

    // ─── Progreso Diario ────────────────────────────────────────────────────

    @Transactional
    public ProgresoDiarioResponseDTO guardarOActualizarProgreso(String username,
                                                                ProgresoDiarioRequestDTO req) {
        User usuario  = getUsuario(username);
        LocalDate hoy = LocalDate.now();

        int semana  = calcularSemana(hoy);
        int diaPlan = calcularDiaPlan(hoy);
        int score   = calcularScore(req.getHorasEfectivas(), req.getQuizPuntaje(),
                req.getLaboratorioCompletado(), req.getSimulacroEstado());

        ProgresoDiario progreso = progresoDiarioRepo
                .findByUsuarioIdAndFecha(usuario.getId(), hoy)
                .orElse(ProgresoDiario.builder().usuario(usuario).fecha(hoy).build());

        progreso.setHorasEfectivas(req.getHorasEfectivas());
        progreso.setQuizPuntaje(req.getQuizPuntaje());
        progreso.setLaboratorioCompletado(req.getLaboratorioCompletado());
        progreso.setSimulacroEstado(req.getSimulacroEstado());
        progreso.setScoreDiario(score);
        progreso.setSemana(semana);
        progreso.setDiaPlan(diaPlan);

        return toResponseDTO(progresoDiarioRepo.save(progreso));
    }

    public Optional<ProgresoDiarioResponseDTO> obtenerProgresoHoy(String username) {
        User usuario = getUsuario(username);
        return progresoDiarioRepo
                .findByUsuarioIdAndFecha(usuario.getId(), LocalDate.now())
                .map(this::toResponseDTO);
    }

    public List<ProgresoDiarioResponseDTO> obtenerTodoElProgreso(String username) {
        User usuario = getUsuario(username);
        return progresoDiarioRepo
                .findByUsuarioIdOrderByFechaAsc(usuario.getId())
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    // ─── Dashboard Completo ─────────────────────────────────────────────────

    public DashboardResumenDTO obtenerDashboard(String username) {
        User usuario       = getUsuario(username);
        Long usuarioId     = usuario.getId();
        LocalDate hoy      = LocalDate.now();
        int semanaActual   = calcularSemana(hoy);
        int diaActual      = calcularDiaPlan(hoy);
        long diasReg       = progresoDiarioRepo.totalDiasRegistrados(usuarioId);
        int progresoPct    = (int) Math.round((diasReg / 56.0) * 100);

        ProgresoDiarioResponseDTO hoyDTO = progresoDiarioRepo
                .findByUsuarioIdAndFecha(usuarioId, hoy)
                .map(this::toResponseDTO).orElse(null);

        // Historial 8 semanas
        List<ResumenSemanalDTO> historial = new ArrayList<>();
        for (int s = 1; s <= 8; s++) {
            List<ProgresoDiario> diasSem = progresoDiarioRepo
                    .findByUsuarioIdAndSemanaOrderByFechaAsc(usuarioId, s);

            List<ProgresoDiarioResponseDTO> diasDTO = diasSem.stream()
                    .map(this::toResponseDTO).collect(Collectors.toList());

            Double prom = progresoDiarioRepo
                    .promedioScorePorSemana(usuarioId, s).orElse(null);

            historial.add(ResumenSemanalDTO.builder()
                    .semana(s)
                    .dias(diasDTO)
                    .promedioScore(prom != null ? Math.round(prom * 10.0) / 10.0 : null)
                    .estado(estadoScore(prom))
                    .build());
        }

        // Módulos
        Map<String, Integer> modulos = moduloProgresoRepo
                .findByUsuarioId(usuarioId)
                .stream()
                .collect(Collectors.toMap(
                        ModuloProgreso::getModuloId,
                        ModuloProgreso::getPorcentaje));

        return DashboardResumenDTO.builder()
                .hoy(hoyDTO)
                .semanaActual(semanaActual)
                .diaActual(diaActual)
                .diasRegistrados((int) diasReg)
                .progresoGlobalPct(progresoPct)
                .historial(historial)
                .modulos(modulos)
                .build();
    }

    // ─── Módulos ────────────────────────────────────────────────────────────

    @Transactional
    public ModuloProgresoDTO actualizarModulo(String username, ModuloProgresoDTO dto) {
        User usuario = getUsuario(username);

        ModuloProgreso modulo = moduloProgresoRepo
                .findByUsuarioIdAndModuloId(usuario.getId(), dto.getModuloId())
                .orElse(ModuloProgreso.builder()
                        .usuario(usuario)
                        .moduloId(dto.getModuloId())
                        .build());

        modulo.setPorcentaje(dto.getPorcentaje());
        moduloProgresoRepo.save(modulo);
        return dto;
    }

    public List<ModuloProgresoDTO> obtenerModulos(String username) {
        User usuario = getUsuario(username);
        return moduloProgresoRepo.findByUsuarioId(usuario.getId()).stream()
                .map(m -> ModuloProgresoDTO.builder()
                        .moduloId(m.getModuloId())
                        .porcentaje(m.getPorcentaje())
                        .build())
                .collect(Collectors.toList());
    }

    // ─── Aprendizajes ───────────────────────────────────────────────────────

    @Transactional
    public NotaDTO agregarAprendizaje(String username, NotaDTO dto) {
        User usuario = getUsuario(username);
        Aprendizaje a = Aprendizaje.builder()
                .usuario(usuario)
                .fecha(LocalDate.now())
                .texto(dto.getTexto())
                .build();
        return toNotaDTO(aprendizajeRepo.save(a));
    }

    public List<NotaDTO> obtenerAprendizajesHoy(String username) {
        User usuario = getUsuario(username);
        return aprendizajeRepo
                .findByUsuarioIdAndFechaOrderByIdAsc(usuario.getId(), LocalDate.now())
                .stream().map(this::toNotaDTO).collect(Collectors.toList());
    }

    // ─── Bloqueos ───────────────────────────────────────────────────────────

    @Transactional
    public NotaDTO agregarBloqueo(String username, NotaDTO dto) {
        User usuario = getUsuario(username);
        Bloqueo b = Bloqueo.builder()
                .usuario(usuario)
                .fecha(LocalDate.now())
                .texto(dto.getTexto())
                .build();
        return toNotaDTO(bloqueoRepo.save(b));
    }

    public List<NotaDTO> obtenerBloqueosHoy(String username) {
        User usuario = getUsuario(username);
        return bloqueoRepo
                .findByUsuarioIdAndFechaOrderByIdAsc(usuario.getId(), LocalDate.now())
                .stream().map(this::toNotaDTO).collect(Collectors.toList());
    }

    // ─── Mappers ─────────────────────────────────────────────────────────────

    private ProgresoDiarioResponseDTO toResponseDTO(ProgresoDiario p) {
        return ProgresoDiarioResponseDTO.builder()
                .id(p.getId())
                .fecha(p.getFecha())
                .horasEfectivas(p.getHorasEfectivas())
                .quizPuntaje(p.getQuizPuntaje())
                .laboratorioCompletado(p.getLaboratorioCompletado())
                .simulacroEstado(p.getSimulacroEstado())
                .scoreDiario(p.getScoreDiario())
                .semana(p.getSemana())
                .diaPlan(p.getDiaPlan())
                .build();
    }

    private NotaDTO toNotaDTO(Aprendizaje a) {
        return NotaDTO.builder()
                .id(a.getId())
                .fecha(a.getFecha())
                .texto(a.getTexto())
                .build();
    }

    private NotaDTO toNotaDTO(Bloqueo b) {
        return NotaDTO.builder()
                .id(b.getId())
                .fecha(b.getFecha())
                .texto(b.getTexto())
                .build();
    }
}