package com.project.EventPlanner.features.feedback.domain.service;

import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.feedback.domain.dto.FeedbackRequestDTO;
import com.project.EventPlanner.features.feedback.domain.dto.FeedbackResponseDTO;
import com.project.EventPlanner.features.feedback.domain.model.Feedback;
import com.project.EventPlanner.features.feedback.domain.repository.FeedbackRepository;
import com.project.EventPlanner.features.registration.domain.mapper.FeedbackMapper;
import com.project.EventPlanner.features.registration.domain.repository.RegistrationRepository;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException; // ✅ Correct

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final RegistrationRepository registrationRepo;
    private final FeedbackMapper mapper;



    public FeedbackResponseDTO createFeedback(FeedbackRequestDTO dto, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepo.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Check if user registered for this event
        boolean registered = registrationRepo.existsByEventIdAndUserId(event.getId(),user.getId());
        if (!registered) {
            throw new AccessDeniedException("You must be registered to submit feedback for this event.");
        }

        // Optional: Prevent duplicate feedback
        if (feedbackRepo.findByUserIdAndEventId(user.getId(), event.getId()).isPresent()) {
            throw new RuntimeException("Feedback already submitted for this event");
        }

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setEvent(event);
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());

        feedbackRepo.save(feedback);

        return mapper.toDTO(feedback);
    }

    public List<FeedbackResponseDTO> getFeedbacksForEvent(Long eventId) {
        return feedbackRepo.findByEventId(eventId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
