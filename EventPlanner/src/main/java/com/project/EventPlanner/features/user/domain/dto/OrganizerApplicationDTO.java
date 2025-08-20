package com.project.EventPlanner.features.user.domain.dto;

import com.project.EventPlanner.common.enums.OrganizerApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrganizerApplicationDTO {
    private Long id;
    private Long userId;
    private String organizerName;
    private String email;
    private String description;
    private OrganizerApplicationStatus status;
    private LocalDateTime appliedAt;
}

