package com.project.EventPlanner.features.feedback.domain.dto;

import lombok.Data;

@Data
public class SentimentResponse {
    private String label;
    private Double score;

    // Getters and Setters
}
