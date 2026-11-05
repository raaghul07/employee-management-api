
package com.raaghul.employeemanagement.controller;

import com.raaghul.employeemanagement.dto.RegisterRequestDTO;
import com.raaghul.employeemanagement.dto.LoginRequestDTO;
import com.raaghul.employeemanagement.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequestDTO request) {

        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                    "message", "User registered successfully"
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginRequestDTO request) {

        String token = authService.login(request);

        return ResponseEntity.ok(
            Map.of(
                "message", "Login successful",
                "token", token,
                "tokenType", "Bearer"
            )
        );
    }
}
