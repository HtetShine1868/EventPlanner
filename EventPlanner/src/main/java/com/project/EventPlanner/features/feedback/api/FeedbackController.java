package com.project.EventPlanner.features.feedback.api;

import com.project.EventPlanner.features.feedback.domain.dto.FeedbackRequestDTO;
import com.project.EventPlanner.features.feedback.domain.dto.FeedbackResponseDTO;
import com.project.EventPlanner.features.feedback.domain.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;


    // Only authenticated users can submit feedback
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> submitFeedback(
            @RequestBody FeedbackRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        FeedbackResponseDTO response = feedbackService.createFeedback(dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Feedback viewing can be public or restricted
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbacksForEvent(@PathVariable Long eventId) {
        List<FeedbackResponseDTO> feedbacks = feedbackService.getFeedbacksForEvent(eventId);
        return ResponseEntity.ok(feedbacks);
    }
}

