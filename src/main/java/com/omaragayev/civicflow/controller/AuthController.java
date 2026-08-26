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
    @Operation(summary = "Generate a bearer JWT token for citizen PIN", description = "Simulates citizen authentication and returns a signed HS256 JWT.")
    public ResponseEntity<Map<String, String>> generateToken(@RequestParam String citizenPin) {
        String token = jwtService.generateToken(citizenPin, Map.of("role", "CITIZEN", "issuer", "CivicFlow-Auth"));
        return ResponseEntity.ok(Map.of(
                "token", token,
                "tokenType", "Bearer",
                "citizenPin", citizenPin
        ));
    }
}