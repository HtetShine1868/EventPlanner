package com.project.EventPlanner.features.feedback.domain.model;

import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.feedback.domain.Rating;
import com.project.EventPlanner.features.user.domain.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    private String comment;

    @Enumerated(EnumType.STRING)
    private Sentiment sentiment;

    // Store sentiment confidence score (percentage)
    private Double sentimentScore;


    private LocalDateTime createdAt = LocalDateTime.now();
}

