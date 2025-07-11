package com.project.EventPlanner.features.admin.api;

import com.project.EventPlanner.features.admin.domain.service.AdminService;
import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.Mapper.EventMapper;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.event.domain.service.EventService;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/events")
public class AdminController {
    private final AdminService adminService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;



    @PutMapping("/organizer-applications/review")
    public ResponseEntity<OrganizerApplicationDTO> reviewApplication(@RequestBody OrganizerApplicationReviewDTO dto) {
        return ResponseEntity.ok(adminService.reviewOrganizerApplication(dto));
    }
    @PutMapping("/{eventId}/approve")
    public ResponseEntity<EventResponseDto> approveEvent(@PathVariable Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setStatus(EventStatus.APPROVED);
        eventRepository.save(event);

        return ResponseEntity.ok(eventMapper.toDto(event));
    }
    @PutMapping("/{eventId}/reject")
    public ResponseEntity<EventResponseDto> rejectEvent(@PathVariable Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setStatus(EventStatus.REJECTED);
        eventRepository.save(event);

        return ResponseEntity.ok(eventMapper.toDto(event));
    }
    @GetMapping("/pending")
    public List<EventResponseDto> getPendingEvents() {
        return eventRepository.findByStatus(EventStatus.PENDING)
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }
}
