package com.project.EventPlanner.features.auth.api;

import com.project.EventPlanner.features.auth.domain.dto.AuthRequest;
import com.project.EventPlanner.features.auth.domain.dto.AuthResponse;
import com.project.EventPlanner.features.auth.domain.service.AuthService;
import com.project.EventPlanner.features.user.domain.dto.UserRegisterDto;
import com.project.EventPlanner.features.user.domain.model.VerifyEmailDTO;
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


        @PostMapping("/register-request")
        public ResponseEntity<String> registerRequest(@RequestBody UserRegisterDto dto) {
            authService.registerRequest(dto);
            return ResponseEntity.ok("Verification code sent to your email");
        }

        /**
         * Step 2: Verify OTP
         * Verifies code and creates real user, returns JWT
         */
        @PostMapping("/verify-email")
        public ResponseEntity<AuthResponse> verifyEmail(@RequestBody VerifyEmailDTO dto) {
            AuthResponse response = authService.verifyEmail(dto);
            return ResponseEntity.ok(response);
        }
    }
