package com.project.EventPlanner.features.event.api;

import com.project.EventPlanner.common.enums.OrganizerApplicationStatus;
import com.project.EventPlanner.features.event.domain.Mapper.EventMapper;
import com.project.EventPlanner.features.event.domain.dto.AgeGroupStatsDTO;
import com.project.EventPlanner.features.event.domain.dto.EventRequestDto;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.dto.RegisteredUserDTO;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.event.domain.service.EventService;
import com.project.EventPlanner.features.registration.domain.service.RegistrationService;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.OrganizerApplicationRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import com.project.EventPlanner.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final OrganizerApplicationRepository organizerApplicationRepository;
    private final EventMapper eventMapper;
    private final RegistrationService registrationService;

    @PostMapping
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto dto,
                                                        @AuthenticationPrincipal  CustomUserDetail userDetail)
    {
        User currentUser = userRepository.findById(userDetail.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        EventResponseDto created = eventService.createEvent(dto, currentUser);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseDto>> getApprovedEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "startTime,asc") String[] sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort[1]), sort[0]));
        Page<EventResponseDto> events = eventService.getApprovedEvents(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZER') or hasAuthority('ADMIN')")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable Long id,
                                                        @RequestBody EventRequestDto dto,
                                                        @AuthenticationPrincipal User currentUser) {
        EventResponseDto updated = eventService.updateEvent(id, dto, currentUser);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/organizers/{organizerId}/events")
    public ResponseEntity<Page<EventResponseDto>> getEventsByOrganizer(
            @PathVariable Long organizerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        // Step 1: Validate if the user exists
        User user = userRepository.findById(organizerId)
                .orElseThrow(() -> new RuntimeException("User with ID " + organizerId + " does not exist."));

        // Step 2: Validate if the user has applied as an organizer
        OrganizerApplication app = organizerApplicationRepository.findByUserId(organizerId)
                .orElseThrow(() -> new RuntimeException("Organizer not found. This user has never applied as an organizer."));

        // Step 3: Check if the organizer is approved
        if (!app.getStatus().equals(OrganizerApplicationStatus.APPROVED)) {
            throw new RuntimeException("Organizer is not approved. Access to events is not allowed.");
        }

        // Step 4: Fetch events by this approved organizer
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());
        Page<EventResponseDto> events = eventService.getEventsByOrganizer(organizerId, pageable);

        return ResponseEntity.ok(events);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EventResponseDto>> filterEventsByCategoryAndLocation(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<EventResponseDto> filteredEvents = eventService.filterEventsByCategoryAndLocation(categoryId, location,latitude,longitude, page, size);
        return ResponseEntity.ok(filteredEvents);
    }
    @GetMapping("/trending")
    public ResponseEntity<List<EventResponseDto>> getTrendingEvents(
            @RequestParam(required = false) Long categoryId,
              @RequestParam(defaultValue = "2") int limit
    ) {

        List<EventResponseDto> events;

        if (categoryId != null) {
            events = eventService.getTopTrendingEventsByCategory(categoryId, limit);
        } else {
            events = eventService.getTopTrendingEvents(limit);
        }

        return ResponseEntity.ok(events);
    }

    @PreAuthorize("hasAuthority('ORGANIZER')")
    @GetMapping("/organizers/my/event-age-analysis")
    public ResponseEntity<Page<AgeGroupStatsDTO>> getMyEventAgeAnalysis(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestParam(value = "eventId", required = false) Long eventIdFilter,
            @RequestParam(value = "categoryId", required = false) Long categoryIdFilter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Long organizerId = userDetail.getId();
        Page<AgeGroupStatsDTO> stats = eventService.getAgeGroupStatsForOrganizer(organizerId, eventIdFilter, categoryIdFilter, pageable);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/organizers/my/events/{eventId}/registrations")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public ResponseEntity<List<RegisteredUserDTO>> getRegisteredUsers(
            @PathVariable Long eventId,
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        User currentUser = userRepository.findById(userDetail.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<RegisteredUserDTO> users = registrationService.getRegisteredUsersByEvent(eventId, currentUser);
        return ResponseEntity.ok(users);
    }

}
