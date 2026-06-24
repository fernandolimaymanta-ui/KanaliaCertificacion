package com.example.kanaliacertificacion.certificacion.config;

import com.example.kanaliacertificacion.security.Role;
import com.example.kanaliacertificacion.security.RoleRepository;
import com.example.kanaliacertificacion.security.User;
import com.example.kanaliacertificacion.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Carga roles, usuarios de prueba y un catalogo de generos/peliculas al
 * arrancar, solo si esta vacio. Usuarios de prueba (username / password):
 *   admin / admin123        -> ROLE_ADMIN
 *   productor / productor123 -> ROLE_PRODUCTOR
 *   analista / analista123   -> ROLE_ANALISTA  (puede ver el reporte)
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    private Role rol(String name) {
        Role r = roleRepo.findByName(name);
        if (r == null) { r = new Role(); r.setName(name); r = roleRepo.save(r); }
        return r;
    }

    private void usuario(String username, String pass, Role... roles) {
        if (userRepo.findByUsername(username) == null) {
            User u = new User();
            u.setUsername(username);
            u.setPassword(encoder.encode(pass));
            u.setEnabled(true);
            u.setRoles(Set.of(roles));
            userRepo.save(u);
        }
    }

    @Override
    public void run(String... args) {
        Role admin = rol("ADMIN");
        Role analista = rol("ESTUDIANTE");
        usuario("admin", "admin123", admin);
        usuario("Fernando.Limaymanta", "estudiante123", analista);

    }
}
