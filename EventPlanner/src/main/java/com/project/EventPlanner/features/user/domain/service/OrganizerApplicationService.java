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

    public OrganizerApplicationDTO createApplication(OrganizerApplicationRequestDTO dto) {
        OrganizerApplication application = mapper.toEntity(dto);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (appRepository.existsByUser(user)) {
            throw new RuntimeException("You have already applied to become an organizer.");
        }
        application.setUser(user);
        application.setStatus(OrganizerApplicationStatus.PENDING);
        application.setAppliedAt(LocalDateTime.now());

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
