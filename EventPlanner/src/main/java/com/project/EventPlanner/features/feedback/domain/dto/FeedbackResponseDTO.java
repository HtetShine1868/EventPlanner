package com.project.EventPlanner.features.feedback.domain.dto;

import com.project.EventPlanner.features.feedback.domain.Rating;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class FeedbackResponseDTO {
    private Long id;
    private String username;
    private Long eventId;
    private String eventTitle;
    private Rating rating;
    private String comment;
    private String sentiment;
    private Double sentimentScore;
    private LocalDateTime createdAt;
}

