package com.project.EventPlanner.features.user.domain.service;

import com.project.EventPlanner.features.user.domain.dto.UserRegisterDto;
import com.project.EventPlanner.features.user.domain.dto.UserResponseDto;
import com.project.EventPlanner.features.user.domain.mapper.UserMapper;
import com.project.EventPlanner.features.user.domain.model.Role;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.RoleRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserResponseDto registerUser(UserRegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        User user = userMapper.toEntity(dto);

        // Set default role (e.g. ROLE_USER)
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRole(role);

        // TODO: Encode password if using Spring Security
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }



}
