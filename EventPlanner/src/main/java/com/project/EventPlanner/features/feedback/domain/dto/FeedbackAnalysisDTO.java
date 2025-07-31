package com.project.EventPlanner.features.feedback.domain.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackAnalysisDTO {
    private Long eventId;
    private String eventTitle;
    private int totalFeedbacks;
    private double averageRating;
    private int positiveCount;
    private int neutralCount;
    private int negativeCount;
}
