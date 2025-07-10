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

    public UserProfileDto CreateUserProfile(UserProfileDto userProfileDto) {
        UserProfile entity = userProfileMapper.toEntity(userProfileDto);
        User user = userRepository.findById(userProfileDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id " +userProfileDto.getUserId()));
        entity.setUser(user);

        UserProfile saved = profileRepository.save(entity);
        return userProfileMapper.toDto(saved);
    }
    public UserProfileDto getUserProfileById(Long id) {
        UserProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id " + id));
        // Use instance method on the injected mapper
        return userProfileMapper.toDto(profile);
    }

    public List<UserProfileDto> getAllUserProfiles() {
        return userProfileMapper.toDTOList(profileRepository.findAll());
    }
    public UserProfileDto updateUserProfile(Long id, UserProfileDto dto) {
        UserProfile existing = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id " + id));

        existing.setFullName(dto.getFullName());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setGender(dto.getGender());

        // If userId changes, update the user association too
        if (!existing.getUser().getId().equals(dto.getUserId())) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + dto.getUserId()));
            existing.setUser(user);
        }


        UserProfile updated = profileRepository.save(existing);
        return userProfileMapper.toDto(updated);
    }

    public void deleteUserProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new RuntimeException("UserProfile not found with id " + id);
        }
        profileRepository.deleteById(id);
    }
}
