package com.project.EventPlanner.features.user.domain.dto;

import com.project.EventPlanner.features.user.domain.ReviewDecision;
import lombok.Data;

@Data
public class OrganizerApplicationReviewDTO {
    private Long applicationId;
    private String decision;
}
