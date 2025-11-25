package com.gestionlivraison.controllers;

import com.gestionlivraison.dto.JwtResponse;
import com.gestionlivraison.dto.LoginRequest;
import com.gestionlivraison.dto.SignUpRequest;
import com.gestionlivraison.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "API d'authentification")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Connexion", description = "Authentification d'un client ou d'un livreur")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticate(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Erreur d'authentification: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Inscription", description = "Création d'un nouveau compte client ou livreur")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            JwtResponse jwtResponse = authService.register(signUpRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Erreur d'inscription: " + e.getMessage()));
        }
    }
}

