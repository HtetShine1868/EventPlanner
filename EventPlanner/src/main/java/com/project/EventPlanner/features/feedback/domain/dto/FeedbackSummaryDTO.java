package com.project.EventPlanner.features.feedback.domain.dto;

import lombok.Data;

@Data
public class FeedbackSummaryDTO {
    private int positiveCount;
    private int neutralCount;
    private int negativeCount;

    // Constructor, Getters, Setters
}
