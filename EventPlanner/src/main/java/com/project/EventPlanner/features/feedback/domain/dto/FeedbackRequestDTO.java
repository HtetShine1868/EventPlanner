package com.project.EventPlanner.features.feedback.domain.dto;

import com.project.EventPlanner.features.feedback.domain.Rating;
import lombok.Data;

@Data
public class FeedbackRequestDTO {
    private Rating rating;
    private String comment;
}

