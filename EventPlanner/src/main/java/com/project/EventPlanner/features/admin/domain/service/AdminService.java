package com.project.EventPlanner.features.admin.domain.service;

import com.project.EventPlanner.common.enums.OrganizerApplicationStatus;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationReviewDTO;
import com.project.EventPlanner.features.user.domain.mapper.OrganizerApplicationMapper;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import com.project.EventPlanner.features.user.domain.model.Role;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.OrganizerApplicationRepository;
import com.project.EventPlanner.features.user.domain.repository.RoleRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

@RequiredArgsConstructor
public class AdminService {
    private final OrganizerApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganizerApplicationMapper mapper;

    public OrganizerApplicationDTO reviewOrganizerApplication(OrganizerApplicationReviewDTO dto) {
        OrganizerApplication app = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

            app.setStatus(dto.getDecision());

            // Set user role to ORGANIZER
            User user = app.getUser();
            Role organizerRole = roleRepository.findByName("ORGANIZER")
                    .orElseThrow(() -> new RuntimeException("ORGANIZER role not found"));
            user.setRole(organizerRole);
            userRepository.save(user);
        return mapper.toDTO(applicationRepository.save(app));
    }

        public List<OrganizerApplicationDTO> getPendingApplications() {
            return applicationRepository.findByStatus(OrganizerApplicationStatus.PENDING)
                    .stream()
                    .map(mapper::toDTO)
                    .toList();
        }


}
