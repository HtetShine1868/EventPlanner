package com.project.EventPlanner.features.auth.api;

import com.project.EventPlanner.features.auth.domain.dto.AuthRequest;
import com.project.EventPlanner.features.auth.domain.dto.AuthResponse;
import com.project.EventPlanner.features.auth.domain.service.AuthService;
import com.project.EventPlanner.features.user.domain.dto.UserRegisterDto;
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

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        }

        @PostMapping("/register")
        public ResponseEntity<AuthResponse> register(@RequestBody UserRegisterDto request) {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        }
    }


