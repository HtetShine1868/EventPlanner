package com.project.EventPlanner.features.auth.domain.service;

import com.project.EventPlanner.features.auth.domain.dto.AuthRequest;
import com.project.EventPlanner.features.auth.domain.dto.AuthResponse;
import com.project.EventPlanner.features.auth.domain.utils.JwtUtil;
import com.project.EventPlanner.features.user.domain.dto.UserRegisterDto;
import com.project.EventPlanner.features.user.domain.model.PendingUser;
import com.project.EventPlanner.features.user.domain.model.Role;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.model.VerifyEmailDTO;
import com.project.EventPlanner.features.user.domain.repository.PendingUserRepository;
import com.project.EventPlanner.features.user.domain.repository.RoleRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import com.project.EventPlanner.features.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final PendingUserRepository pendingUserRepo;
    private final UserRepository userRepo;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtUtil; // your JWT helper class
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepo;

    public void registerRequest(UserRegisterDto dto) {
        if (userRepo.existsByEmail(dto.getEmail()) || userRepo.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Email or username already taken");
        }

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        PendingUser pending = new PendingUser();
        pending.setName(dto.getUsername());
        pending.setEmail(dto.getEmail());
        pending.setPassword(passwordEncoder.encode(dto.getPassword()));
        pending.setOtpCode(otp);
        pending.setExpiryDate(LocalDateTime.now().plusMinutes(10));

        pendingUserRepo.save(pending);

        sendVerificationEmail(dto.getEmail(), otp);
    }

    private void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Verification Code");
        message.setText("Your verification code is: " + code);
        mailSender.send(message);
    }

    // Step 2: Verify OTP → create User
    public AuthResponse verifyEmail(VerifyEmailDTO dto) {
        PendingUser pending = pendingUserRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("No pending registration found"));

        if (!pending.getOtpCode().equals(dto.getCode()) ||
                pending.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        // Create User in real table
        User user = new User();
        user.setUsername(pending.getName());
        user.setEmail(pending.getEmail());
        user.setPassword(pending.getPassword());
        user.setRole(defaultUserRole()); // assign default ROLE_USER
        user.setUserprofile(null);
        user.setOrganizerApplication(null);

        userRepo.save(user);

        // cleanup
        pendingUserRepo.delete(pending);

        // Generate JWT
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }

    private Role defaultUserRole() {
        return roleRepo.findById(2L) // or findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));
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
