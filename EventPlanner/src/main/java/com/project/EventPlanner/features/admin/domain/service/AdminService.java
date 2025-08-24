package com.project.EventPlanner.features.admin.domain.service;

import com.project.EventPlanner.common.enums.OrganizerApplicationStatus;
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

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    // ✅ Approve or reject organizer application
    public OrganizerApplicationDTO reviewOrganizerApplication(OrganizerApplicationReviewDTO dto) {
        OrganizerApplication app = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(dto.getDecision());

        if (dto.getDecision() == OrganizerApplicationStatus.APPROVED) {
            User user = app.getUser();
            Role organizerRole = roleRepository.findByName("ORGANIZER")
                    .orElseThrow(() -> new RuntimeException("ORGANIZER role not found"));
            user.setRole(organizerRole);
            userRepository.save(user);
        }

        return organizerMapper.toDTO(applicationRepository.save(app));
    }

    // ✅ Get all pending organizer applications
    public List<OrganizerApplicationDTO> getPendingApplications() {
        return applicationRepository.findByStatus(OrganizerApplicationStatus.PENDING)
                .stream()
                .map(organizerMapper::toDTO)
                .toList();
    }

    // ✅ Approve event
    public EventResponseDto approveEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(EventStatus.APPROVED);
        return eventMapper.toDto(eventRepository.save(event));
    }

    // ✅ Reject event
    public EventResponseDto rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(EventStatus.REJECTED);
        return eventMapper.toDto(eventRepository.save(event));
    }

    // ✅ Get pending events
    public List<EventResponseDto> getPendingEvents() {
        return eventRepository.findByStatus(EventStatus.PENDING)
                .stream()
                .map(eventMapper::toDto)
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
