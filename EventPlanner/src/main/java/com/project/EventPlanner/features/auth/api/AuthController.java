package com.project.EventPlanner.features.auth.api;

import com.project.EventPlanner.features.auth.domain.dto.AuthRequest;
import com.project.EventPlanner.features.auth.domain.dto.AuthResponse;
import com.project.EventPlanner.features.auth.domain.service.AuthService;
import com.project.EventPlanner.features.user.domain.dto.UserRegisterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


    @RestController
    @RequestMapping("/api/auth")
    @RequiredArgsConstructor
    public class AuthController {

        private final AuthService authService;


        @Operation(summary = "Login", description = "Authenticate user and return JWT token")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Login successful"),
                @ApiResponse(responseCode = "401", description = "Invalid credentials")
        })
        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        }


        @Operation(summary = "Register", description = "Register a new user")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "User registered successfully"),
                @ApiResponse(responseCode = "400", description = "Validation failed")
        })
        @PostMapping("/register")
        public ResponseEntity<AuthResponse> register(@RequestBody UserRegisterDto request) {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        }
    }


