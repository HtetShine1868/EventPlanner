package com.project.EventPlanner.features.user.domain.service;

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




    public OrganizerApplicationDTO apply(OrganizerApplicationDTO dto) {
       User user = userRepository.findById(dto.getUserId())
               .orElseThrow(() -> new RuntimeException("User Not Found"));


        if (appRepository.existsByUserId(dto.getUserId())) {
            throw new RuntimeException("User already applied");
        }
        OrganizerApplication app = mapper.toEntity(dto);
        app.setUser(user);
        app.setStatus("PENDING");
        app.setAppliedAt(LocalDateTime.now());

        return mapper.toDTO(appRepository.save(app));
    }

    public OrganizerApplicationDTO getById(Long id) {
        return appRepository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }


    public List<OrganizerApplicationDTO> getAll() {
        return mapper.toDTOList(appRepository.findAll());
    }


    public OrganizerApplicationDTO approveApplication(Long id) {
        OrganizerApplication app = appRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus("APPROVED");
        return mapper.toDTO(appRepository.save(app));
    }

    public OrganizerApplicationDTO rejectApplication(Long id) {
        OrganizerApplication app = appRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus("REJECTED");
        return mapper.toDTO(appRepository.save(app));
    }
}
