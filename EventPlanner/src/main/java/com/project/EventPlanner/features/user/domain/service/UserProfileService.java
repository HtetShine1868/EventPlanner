package com.project.EventPlanner.features.user.domain.service;

import com.project.EventPlanner.features.user.domain.dto.UserProfileDto;
import com.project.EventPlanner.features.user.domain.mapper.UserMapper;
import com.project.EventPlanner.features.user.domain.mapper.UserProfileMapper;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.model.UserProfile;
import com.project.EventPlanner.features.user.domain.repository.UserProfileRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository profileRepository;
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserRepository userRepository;
    public UserProfileDto createUserProfile(UserProfileDto dto, User currentUser) {
        UserProfile entity = userProfileMapper.toEntity(dto);
        entity.setUser(currentUser); // assign current user from JWT

        UserProfile saved = profileRepository.save(entity);
        return userProfileMapper.toDto(saved);
    }
    public UserProfileDto getUserProfileById(Long id) {
        UserProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id " + id));
        return userProfileMapper.toDto(profile);
    }
    public List<UserProfileDto> getAllUserProfiles() {
        return userProfileMapper.toDTOList(profileRepository.findAll());
    }

    public UserProfileDto updateUserProfile(Long id, UserProfileDto dto, User currentUser) {
        UserProfile existing = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id " + id));

        // Check if current user owns this profile
        if (!existing.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized to update this profile");
        }

        existing.setFullName(dto.getFullName());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setGender(dto.getGender());

        UserProfile updated = profileRepository.save(existing);
        return userProfileMapper.toDto(updated);
    }
    public void deleteUserProfile(Long id, User currentUser) {
        UserProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id " + id));

        if (!profile.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized to delete this profile");
        }
        profileRepository.deleteById(id);
    }

}
