package com.omaragayev.civicflow.controller;

import com.omaragayev.civicflow.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Public identity and JWT token issuance endpoints")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/token")
    @Operation(summary = "Generate a bearer JWT token with custom role", description = "Issues signed tokens for testing CITIZEN, OFFICER, or ADMIN roles.")
    public ResponseEntity<Map<String, String>> generateToken(
            @RequestParam String citizenPin,
            @RequestParam(defaultValue = "CITIZEN") String role
    ) {
        String token = jwtService.generateToken(citizenPin, Map.of("role", role.toUpperCase(), "issuer", "CivicFlow-Auth"));
        return ResponseEntity.ok(Map.of(
                "token", token,
                "tokenType", "Bearer",
                "citizenPin", citizenPin,
                "role", "ROLE_" + role.toUpperCase()
        ));
    }
}