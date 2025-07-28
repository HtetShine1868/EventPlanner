package com.project.EventPlanner.features.feedback.domain.repository;

import com.project.EventPlanner.features.feedback.domain.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByEventId(Long eventId);
    List<Feedback> findByUserId(Long userId);
    Optional<Feedback> findByUserIdAndEventId(Long userId, Long eventId);

}
