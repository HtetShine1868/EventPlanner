package com.project.EventPlanner.features.event.domain.service;

import com.project.EventPlanner.exception.EventConflictException;
import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.Mapper.EventMapper;
import com.project.EventPlanner.features.event.domain.dto.EventRequestDto;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventCategoryRepository;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.project.EventPlanner.features.event.domain.EventSpecification;



import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    public EventResponseDto createEvent(EventRequestDto eventRequestDto) {
        Event event = eventMapper.toEntity(eventRequestDto);

        // Set category
        event.setCategory(
                eventCategoryRepository.findById(eventRequestDto.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found"))
        );

        // Set organizer
        event.setOrganizer(
                userRepository.findById(eventRequestDto.getOrganizerId())
                        .orElseThrow(() -> new RuntimeException("Organizer not found"))
        );

        // ✅ Conflict check (check PENDING and APPROVED)
        List<Event> conflicts = eventRepository.findConflictingEvents(
                event.getLocation(),
                event.getLatitude(),
                event.getLongitude(),
                event.getStartTime(),
                event.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            throw new EventConflictException("Conflict: Another event already exists at this location and time.");
        }

        // Final fields
        event.setStatus(EventStatus.PENDING);
        event.setRegisteredCount(0);

        return eventMapper.toDto(eventRepository.save(event));
    }

    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return eventMapper.toDto(event);
    }
    public List<EventResponseDto> getAllEvents() {
        Iterable<Event> iterable = eventRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }
    public EventResponseDto updateEvent(Long id, EventRequestDto dto) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Event updatedEvent = eventMapper.toEntity(dto);
        updatedEvent.setId(id);
        updatedEvent.setRegisteredCount(existingEvent.getRegisteredCount());

        // Status handling
        if (dto.getStatus() != null) {
            updatedEvent.setStatus(dto.getStatus());
        } else {
            updatedEvent.setStatus(existingEvent.getStatus());
        }

        // Set category
        updatedEvent.setCategory(
                eventCategoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found"))
        );

        // Set organizer
        updatedEvent.setOrganizer(
                userRepository.findById(dto.getOrganizerId())
                        .orElseThrow(() -> new RuntimeException("Organizer not found"))
        );

        // ✅ Conflict check (exclude current event's ID)
        List<Event> conflicts = eventRepository.findConflictingEventsExcludingSelf(
                updatedEvent.getLocation(),
                updatedEvent.getLatitude(),
                updatedEvent.getLongitude(),
                updatedEvent.getStartTime(),
                updatedEvent.getEndTime(),
                id
        );

        if (!conflicts.isEmpty()) {
            throw new EventConflictException("Conflict: Another event already exists at this location and time.");
        }

        return eventMapper.toDto(eventRepository.save(updatedEvent));
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        eventRepository.deleteById(id);
    }
    public Page<EventResponseDto> getApprovedEvents(Pageable pageable) {
        return eventRepository.findByStatus(EventStatus.APPROVED, pageable)
                .map(eventMapper::toDto);
    }

    public List<EventResponseDto> getEventsByOrganizer(Long organizerId) {
        List<Event> events = eventRepository.findByOrganizerId(organizerId);
        return events.stream()
                .map(eventMapper::toDto)
                .toList();
    }
    public Page<EventResponseDto> filterEventsByCategoryAndLocation(Long categoryId, String location,Double latitude,Double longitude, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> filteredEvents;
        EventStatus approved = EventStatus.APPROVED;

        if (categoryId != null && location != null && !location.isBlank()) {
            filteredEvents = eventRepository
                    .findByCategoryIdAndLocationIsNotNullAndLocationContainingIgnoreCaseAndStatus(categoryId, location,latitude,longitude, approved, pageable);
        } else if (categoryId != null) {
            filteredEvents = eventRepository.findByCategoryIdAndStatus(categoryId, approved, pageable);
        } else if (location != null && !location.isBlank()) {
            filteredEvents = eventRepository.findByLocationIsNotNullAndLocationContainingIgnoreCaseAndStatus(location,latitude,longitude, approved, pageable);
        } else {
            filteredEvents = eventRepository.findByStatus(approved, pageable);
        }

        return filteredEvents.map(eventMapper::toDto);
    }
}
