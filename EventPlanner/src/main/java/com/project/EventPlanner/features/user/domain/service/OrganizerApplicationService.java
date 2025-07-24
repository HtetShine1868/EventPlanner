package com.project.EventPlanner.features.user.domain.service;

import com.project.EventPlanner.common.enums.OrganizerApplicationStatus;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
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

    public OrganizerApplicationDTO createApplication(OrganizerApplicationDTO dto, User currentUser) {
        OrganizerApplication entity = mapper.toEntity(dto);
        entity.setUser(currentUser);
        entity.setStatus(OrganizerApplicationStatus.PENDING); // initial status
        entity.setAppliedAt(LocalDateTime.now());
        OrganizerApplication saved = appRepository.save(entity);
        return mapper.toDTO(saved);
    }


    public OrganizerApplicationDTO getById(Long id) {
        return appRepository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    public List<OrganizerApplicationDTO> getAll() {

        return mapper.toDTOList(appRepository.findAll());
    }
}
