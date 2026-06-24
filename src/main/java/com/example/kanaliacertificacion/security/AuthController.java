package com.example.kanaliacertificacion.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUserDetailsService uds;
    private final JwtTokenUtil jwt;

    public AuthController(AuthenticationManager am, JwtUserDetailsService uds, JwtTokenUtil jwt) {
        this.authManager = am;
        this.uds = uds;
        this.jwt = jwt;
    }

    @PostMapping("/ingresos")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
            body.get("username"), body.get("password")));
        UserDetails ud = uds.loadUserByUsername(body.get("username"));
        return Map.of("token", jwt.generateToken(ud));
    }
}
