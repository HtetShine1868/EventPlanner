package com.project.EventPlanner.features.event.api;

import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.Mapper.EventMapper;
import com.project.EventPlanner.features.event.domain.dto.EventRequestDto;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.event.domain.service.EventService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto dto) {
        return ResponseEntity.ok(eventService.createEvent(dto));
    }

    @GetMapping
    public List<EventResponseDto> getApprovedEvents() {
        return eventRepository.findByStatus(EventStatus.APPROVED)
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable Long id,
            @RequestBody EventRequestDto dto) {
        return ResponseEntity.ok(eventService.updateEvent(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/organizers/{organizerId}/events")
    public ResponseEntity<List<EventResponseDto>> getOrganizerEvents(@PathVariable Long organizerId) {
        List<EventResponseDto> events = eventService.getEventsByOrganizer(organizerId);
        return ResponseEntity.ok(events);
    }


}
