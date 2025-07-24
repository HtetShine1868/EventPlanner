package com.project.EventPlanner.features.auth.domain.service;

import com.project.EventPlanner.features.auth.domain.dto.AuthRequest;
import com.project.EventPlanner.features.auth.domain.dto.AuthResponse;
import com.project.EventPlanner.features.auth.domain.utils.JwtUtil;
import com.project.EventPlanner.features.user.domain.dto.UserRegisterDto;
import com.project.EventPlanner.features.user.domain.model.Role;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.RoleRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import com.project.EventPlanner.features.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil; // your JWT helper class
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(UserRegisterDto dto) {
        // Delegate user creation to UserService
        User newUser = userService.registerUser(dto);

        // After saving user, generate JWT token
        String token = jwtUtil.generateToken(newUser);

        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userService.loadUserByUsername(request.getUsername());

        // ✅ Generate token using full user (includes role claim)
        String token = jwtUtil.generateToken(user);

        return new AuthResponse(token);
    }
}
