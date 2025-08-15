package com.project.EventPlanner.features.feedback.domain.service;

import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.feedback.domain.dto.FeedbackAnalysisDTO;
import com.project.EventPlanner.features.feedback.domain.dto.FeedbackRequestDTO;
import com.project.EventPlanner.features.feedback.domain.dto.FeedbackResponseDTO;
import com.project.EventPlanner.features.feedback.domain.dto.FeedbackSummaryDTO;
import com.project.EventPlanner.features.feedback.domain.model.Feedback;
import com.project.EventPlanner.features.feedback.domain.model.Sentiment;
import com.project.EventPlanner.features.feedback.domain.model.SentimentResult;
import com.project.EventPlanner.features.feedback.domain.repository.FeedbackRepository;
import com.project.EventPlanner.features.feedback.domain.Mapper.FeedbackMapper;
import com.project.EventPlanner.features.registration.domain.repository.RegistrationRepository;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;
    
    private final FeedbackRepository feedbackRepo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final RegistrationRepository registrationRepo;
    private final SentimentClient sentimentClient;

    public FeedbackResponseDTO createFeedback(Long eventId,FeedbackRequestDTO dto, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        boolean registered = registrationRepo.existsByEventIdAndUserId(event.getId(), user.getId());
        if (!registered) {
            throw new AccessDeniedException("You must be registered to submit feedback for this event.");
        }

        if (feedbackRepo.findByUserIdAndEventId(user.getId(), event.getId()).isPresent()) {
            throw new RuntimeException("Feedback already submitted for this event");
        }

        SentimentResult result = sentimentClient.analyzeSentiment(dto.getComment());

        Sentiment sentiment;
        try {
            sentiment = Sentiment.valueOf(result.getLabel().toUpperCase());
        } catch (IllegalArgumentException e) {
            sentiment = Sentiment.NEUTRAL; // fallback if label is unexpected
        }

        Feedback feedback = Feedback.builder()
                .user(user)
                .event(event)
                .comment(dto.getComment())
                .rating(dto.getRating())
                .sentiment(sentiment)
                .sentimentScore(result.getScore())
                .createdAt(LocalDateTime.now())
                .build();

        Feedback saved = feedbackRepo.save(feedback);

        return feedbackMapper.toDTO(saved);
    }

    public List<FeedbackResponseDTO> getFeedbacksForEvent(Long eventId) {
        List<Feedback> feedbacks = feedbackRepo.findByEventId(eventId);
        return feedbackMapper.toDTOs(feedbacks);
    }

    public FeedbackResponseDTO getUserFeedbackForEvent(Long eventId, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Feedback feedback = feedbackRepo.findByUserIdAndEventId(user.getId(), eventId)
                .orElse(null);

        if (feedback == null) {
            return null;
        }
        return feedbackMapper.toDTO(feedback);
    }


    public FeedbackAnalysisDTO analyzeFeedbackForEvent(Long eventId) {
        List<Feedback> feedbacks = feedbackRepo.findByEventId(eventId);

        if (feedbacks.isEmpty()) {
            throw new RuntimeException("No feedback found for this event.");
        }

        int total = feedbacks.size();
        double avgRating = feedbacks.stream()
                .mapToInt(f -> f.getRating().getNumericValue()) // convert enum to number
                .average()
                .orElse(0.0);

        int positive = 0, neutral = 0, negative = 0;
        for (Feedback f : feedbacks) {
            switch (f.getSentiment()) {
                case POSITIVE -> positive++;
                case NEUTRAL -> neutral++;
                case NEGATIVE -> negative++;
            }
        }

        return FeedbackAnalysisDTO.builder()
                .eventId(eventId)
                .eventTitle(feedbacks.get(0).getEvent().getTitle())
                .totalFeedbacks(total)
                .averageRating(avgRating)
                .positiveCount(positive)
                .neutralCount(neutral)
                .negativeCount(negative)
                .build();
    }

    public FeedbackSummaryDTO getFeedbackSummary(Long eventId) {
        List<Feedback> feedbacks = feedbackRepo.findByEventId(eventId);

        int positive = 0, neutral = 0, negative = 0;

        for (Feedback f : feedbacks) {
            switch (f.getSentiment()) {
                case POSITIVE -> positive++;
                case NEUTRAL -> neutral++;
                case NEGATIVE -> negative++;
            }
        }

        FeedbackSummaryDTO summary = new FeedbackSummaryDTO();
        summary.setPositiveCount(positive);
        summary.setNeutralCount(neutral);
        summary.setNegativeCount(negative);

        return summary;
    }


}
