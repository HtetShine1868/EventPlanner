package com.project.EventPlanner.features.feedback.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentResult {
    private String label; // "positive", "neutral", etc.
    private double score; // 0–100 (confidence %)
}


