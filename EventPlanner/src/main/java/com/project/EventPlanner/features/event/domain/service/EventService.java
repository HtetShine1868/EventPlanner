package com.project.EventPlanner.features.event.domain.service;

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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.project.EventPlanner.features.event.domain.EventSpecification;



import org.springframework.data.domain.Pageable;

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
        Event event =eventMapper.toEntity(eventRequestDto);
        event.setCategory(
                eventCategoryRepository.findById(eventRequestDto.getCategoryId()).
                        orElseThrow(()->new RuntimeException("Category not found"))
        );
        event.setOrganizer(
                userRepository.findById(eventRequestDto.getOrganizerId())
                        .orElseThrow(() -> new RuntimeException("Organizer not found"))
        );
        event.setStatus(EventStatus.PENDING);
        event.setRegisteredCount(0); // initialize
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
        // 1. Fetch existing event
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // 2. Map new data from DTO (creates a new Event object)
        Event updatedEvent = eventMapper.toEntity(dto);

        // 3. Preserve immutable or sensitive fields
        updatedEvent.setId(id); // ensure update
        updatedEvent.setRegisteredCount(existingEvent.getRegisteredCount()); // preserve count

        // 4. Preserve existing status if not provided
        if (dto.getStatus() != null) {
            updatedEvent.setStatus(dto.getStatus());
        } else {
            updatedEvent.setStatus(existingEvent.getStatus()); // preserve status
        }

        // 5. Set related entities
        updatedEvent.setCategory(
                eventCategoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found"))
        );

        updatedEvent.setOrganizer(
                userRepository.findById(dto.getOrganizerId())
                        .orElseThrow(() -> new RuntimeException("Organizer not found"))
        );

        // 6. Save and return
        return eventMapper.toDto(eventRepository.save(updatedEvent));
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        eventRepository.deleteById(id);
    }

    public Page<EventResponseDto> searchEventsForUser(String keyword, Long categoryId, Pageable pageable) {
        Specification<Event> spec =
                EventSpecification.hasKeyword(keyword)
                        .and(EventSpecification.hasCategoryId(categoryId))
                        .and(EventSpecification.hasStatus("APPROVED"));

        Page<Event> page = eventRepository.findAll(spec, pageable); 

        return page.map(eventMapper::toDto);
    }


    public Page<EventResponseDto> searchEventsForAdmin(String keyword, Long categoryId, String status, Pageable pageable) {
        Specification<Event> spec = EventSpecification
                .hasKeyword(keyword)
                .and(EventSpecification.hasCategoryId(categoryId))
                .and(EventSpecification.hasStatus(status)); // Admin can filter by status

        return eventRepository.findAll(spec, pageable)
                .map(eventMapper::toDto);
    }
}
