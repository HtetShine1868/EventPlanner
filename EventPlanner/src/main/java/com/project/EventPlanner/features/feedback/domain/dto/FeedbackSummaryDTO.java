package com.project.EventPlanner.features.feedback.domain.dto;

import lombok.Data;

@Data
public class FeedbackSummaryDTO {
    private double averageRating;
    private int positiveCount;
    private int neutralCount;
    private int negativeCount;
    private double positivePercent;
    private double neutralPercent;
    private double negativePercent;
}

