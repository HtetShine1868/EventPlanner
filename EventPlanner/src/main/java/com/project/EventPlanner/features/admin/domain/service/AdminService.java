package com.project.EventPlanner.features.admin.domain.service;

import com.project.EventPlanner.common.enums.OrganizerApplicationStatus;
import com.project.EventPlanner.common.notification.NotificationService;
import com.project.EventPlanner.features.admin.domain.dto.DashboardStatsDTO;
import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.Mapper.EventMapper;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationReviewDTO;
import com.project.EventPlanner.features.user.domain.mapper.OrganizerApplicationMapper;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import com.project.EventPlanner.features.user.domain.model.Role;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.OrganizerApplicationRepository;
import com.project.EventPlanner.features.user.domain.repository.RoleRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final OrganizerApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganizerApplicationMapper organizerMapper;
    private final NotificationService notificationService;


    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Transactional
    public EventResponseDto approveEvent(Long eventId, String feedback) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setStatus(EventStatus.APPROVED);
        event.setFeedback(feedback);
        Event savedEvent = eventRepository.save(event);

        // Create notification for the organizer
        User organizerUser = userRepository.findById(event.getOrganizer().getId())
                .orElseThrow(() -> new RuntimeException("Organizer user not found"));

        String message = "Your event '" + event.getTitle() + "' has been APPROVED. ";
        if (feedback != null && !feedback.trim().isEmpty()) {
            message += "Feedback: " + feedback;
        }

        notificationService.createNotification(organizerUser, message);

        return eventMapper.toDto(savedEvent);
    }

    // ✅ Reject event with notification
    @Transactional
    public EventResponseDto rejectEvent(Long eventId, String feedback) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setStatus(EventStatus.REJECTED);
        event.setFeedback(feedback);
        Event savedEvent = eventRepository.save(event);

        // Create notification for the organizer
        User organizerUser = userRepository.findById(event.getOrganizer().getId())
                .orElseThrow(() -> new RuntimeException("Organizer user not found"));

        String message = "Your event '" + event.getTitle() + "' has been REJECTED. ";
        if (feedback != null && !feedback.trim().isEmpty()) {
            message += "Feedback: " + feedback;
        } else {
            message += "Please contact admin for more details.";
        }

        notificationService.createNotification(organizerUser, message);

        return eventMapper.toDto(savedEvent);
    }

    // ✅ Approve or reject organizer application with notification
    @Transactional
    public OrganizerApplicationDTO reviewOrganizerApplication(OrganizerApplicationReviewDTO dto) {
        OrganizerApplication app = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(dto.getDecision());
        app.setFeedback(dto.getFeedback());

        String message;
        if (dto.getDecision() == OrganizerApplicationStatus.APPROVED) {
            User user = app.getUser();
            Role organizerRole = roleRepository.findByName("ORGANIZER")
                    .orElseThrow(() -> new RuntimeException("ORGANIZER role not found"));
            user.setRole(organizerRole);
            userRepository.save(user);

            message = "Your organizer application has been APPROVED. ";
        } else {
            message = "Your organizer application has been REJECTED. ";
        }

        if (dto.getFeedback() != null && !dto.getFeedback().trim().isEmpty()) {
            message += "Feedback: " + dto.getFeedback();
        }

        notificationService.createNotification(app.getUser(), message);

        return organizerMapper.toDTO(applicationRepository.save(app));
    }

    // ✅ Get pending events
    public List<EventResponseDto> getPendingEvents() {
        return eventRepository.findByStatus(EventStatus.PENDING)
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }

    // ✅ Get all pending organizer applications
    public List<OrganizerApplicationDTO> getPendingApplications() {
        return applicationRepository.findByStatus(OrganizerApplicationStatus.PENDING)
                .stream()
                .map(organizerMapper::toDTO)
                .toList();
    }
    public DashboardStatsDTO getDashboardStatistics() {
        long totalEvents = eventRepository.count();
        long pendingEvents = eventRepository.countByStatus(EventStatus.PENDING);
        long approvedEvents = eventRepository.countByStatus(EventStatus.APPROVED);
        long totalAttendees = eventRepository.sumAttendees();

        return DashboardStatsDTO.builder()
                .totalEvents(totalEvents)
                .pendingReview(pendingEvents)
                .approved(approvedEvents)
                .totalAttendees(totalAttendees)
                .build();
    }

    public Long getTotalEventsCount() {
        return eventRepository.count();
    }

    public Long getPendingEventsCount() {
        return eventRepository.countByStatus(EventStatus.PENDING);
    }

    public Long getApprovedEventsCount() {
        return eventRepository.countByStatus(EventStatus.APPROVED);
    }

    public Long getTotalAttendeesCount() {
        return eventRepository.sumAttendees();
    }
}
