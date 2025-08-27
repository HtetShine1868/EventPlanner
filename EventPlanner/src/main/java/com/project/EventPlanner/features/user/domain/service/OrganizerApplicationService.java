package com.project.EventPlanner.features.user.domain.service;

import com.project.EventPlanner.common.enums.OrganizerApplicationStatus;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationRequestDTO;
import com.project.EventPlanner.features.user.domain.mapper.OrganizerApplicationMapper;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import com.project.EventPlanner.features.user.domain.model.Role;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.OrganizerApplicationRepository;
import com.project.EventPlanner.features.user.domain.repository.RoleRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class OrganizerApplicationService {
    private final OrganizerApplicationRepository appRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganizerApplicationMapper mapper;

    public OrganizerApplicationDTO createApplication(OrganizerApplicationRequestDTO dto, String username) {
        // Find logged-in user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user has a pending or approved application
        boolean hasActiveApplication = appRepository.existsByUserAndStatusIn(
                user,
                List.of(OrganizerApplicationStatus.PENDING, OrganizerApplicationStatus.APPROVED)
        );

        if (hasActiveApplication) {
            throw new RuntimeException("You already have an active organizer application.");
        }

        // Map DTO -> Entity
        OrganizerApplication application = mapper.toEntity(dto);
        application.setUser(user);
        application.setStatus(OrganizerApplicationStatus.PENDING);
        application.setAppliedAt(LocalDateTime.now());
        application.setOrganizerName(dto.getOrganizerName());
        application.setEmail(dto.getEmail());
        application.setDescription(dto.getDescription());
        application.setFeedback(null);

        // Clear feedback for new application
        application.setFeedback(null);

        OrganizerApplication savedApp = appRepository.save(application);
        return mapper.toDTO(savedApp);
    }



    public OrganizerApplicationDTO getById(Long id) {
        return appRepository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    public List<OrganizerApplicationDTO> getAll() {

        List<OrganizerApplication> pendingApps = appRepository.findByStatus(OrganizerApplicationStatus.PENDING);
        return mapper.toDTOList(pendingApps);
    }
}
