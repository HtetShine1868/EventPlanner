package com.project.EventPlanner.features.user.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrganizerApplicationDTO {
    private Long id;
    private Long userId;
    private String organizerName;
    private String status;
    private LocalDateTime appliedAt;
}
