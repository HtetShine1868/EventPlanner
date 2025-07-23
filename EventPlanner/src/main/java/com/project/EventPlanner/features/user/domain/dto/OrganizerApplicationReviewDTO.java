package com.project.EventPlanner.features.user.domain.dto;

import com.project.EventPlanner.common.enums.OrganizerApplicationStatus;
import lombok.Data;

@Data
public class OrganizerApplicationReviewDTO {
    private Long applicationId;
    private OrganizerApplicationStatus decision; // in Review DTO

}
