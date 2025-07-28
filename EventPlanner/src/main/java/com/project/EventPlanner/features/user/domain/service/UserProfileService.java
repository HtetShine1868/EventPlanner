package com.project.EventPlanner.features.user.domain.service;

import com.project.EventPlanner.features.user.domain.dto.UserProfileRequestDTO;
import com.project.EventPlanner.features.user.domain.dto.UserProfileResponseDTO;
import com.project.EventPlanner.features.user.domain.mapper.UserProfileMapper;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.model.UserProfile;
import com.project.EventPlanner.features.user.domain.repository.UserProfileRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final UserProfileMapper mapper;

    public UserProfileResponseDTO create(Long userId, UserProfileRequestDTO dto) {
        if (userProfileRepository.existsByUserId(userId)) {
            throw new RuntimeException("User profile already exists.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = mapper.toEntity(dto);
        profile.setUser(user);
        user.setUserprofile(profile);
        return mapper.toDto(userProfileRepository.save(profile));
    }

    public UserProfileResponseDTO getByUserId(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapper.toDto(profile);
    }

    public UserProfileResponseDTO update(Long userId, UserProfileRequestDTO dto) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        mapper.updateUserProfileFromDto(dto, profile);
        return mapper.toDto(userProfileRepository.save(profile));
    }
}
