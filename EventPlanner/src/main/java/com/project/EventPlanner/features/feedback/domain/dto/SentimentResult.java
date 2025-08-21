package com.project.EventPlanner.features.feedback.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SentimentResult {
    private String label;
    private Double score;
}
