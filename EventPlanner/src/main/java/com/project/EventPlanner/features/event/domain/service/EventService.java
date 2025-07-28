package com.project.EventPlanner.features.event.domain.service;

import com.project.EventPlanner.exception.EventConflictException;
import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.Mapper.EventMapper;
import com.project.EventPlanner.features.event.domain.dto.AgeGroupStatsDTO;
import com.project.EventPlanner.features.event.domain.dto.EventRequestDto;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventCategoryRepository;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.registration.domain.model.Registration;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    public EventResponseDto createEvent(EventRequestDto eventRequestDto,  User currentUser) {
        Event event = eventMapper.toEntity(eventRequestDto);
        // Set category
        event.setCategory(
                eventCategoryRepository.findById(eventRequestDto.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found"))
        );


        event.setOrganizer( currentUser);
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
        System.out.println("Creating event with createdBy: " + currentUser.getId());
        event.setCreatedBy(currentUser);
        System.out.println("Event createdBy before save: " + event.getCreatedBy().getId());


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
    public EventResponseDto updateEvent(Long id, EventRequestDto dto,User currentUser) {
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
        updatedEvent.setOrganizer(existingEvent.getOrganizer());

        //  Conflict check (exclude current event's ID)
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

    public Page<EventResponseDto> getEventsByOrganizer(Long organizerId, Pageable pageable) {
        return eventRepository.findByOrganizerId(organizerId, pageable)
                .map(eventMapper::toDto);
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
    public List<EventResponseDto> getTopTrendingEvents(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Event> events = eventRepository.findTopTrendingEvents(pageable);
        return events.stream().map(eventMapper::toDto).toList();
    }

    public List<EventResponseDto> getTopTrendingEventsByCategory(Long categoryId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Event> events = eventRepository.findTopTrendingEventsByCategory(categoryId, pageable);
        return events.stream().map(eventMapper::toDto).toList();
    }

    public EventResponseDto getMostRegisteredEvent(Long organizerId) {
        Pageable limit = PageRequest.of(0, 1);
        List<Event> result = eventRepository.findTopEventByOrganizer(organizerId, limit);
        return result.isEmpty() ? null : eventMapper.toDto(result.get(0));
    }

    public Page<AgeGroupStatsDTO> getAgeGroupStatsForOrganizer(Long organizerId, Long eventIdFilter, Long categoryIdFilter, Pageable pageable) {
        List<Event> events = eventRepository.findByCreatedById(organizerId);

        if (eventIdFilter != null) {
            events = events.stream()
                    .filter(event -> event.getId().equals(eventIdFilter))
                    .toList();
        }

        if (categoryIdFilter != null) {
            events = events.stream()
                    .filter(event -> event.getCategory() != null && event.getCategory().getId().equals(categoryIdFilter))
                    .toList();
        }

        List<AgeGroupStatsDTO> allResults = new ArrayList<>();

        for (Event event : events) {
            Map<String, Long> ageGroups = new LinkedHashMap<>();
            ageGroups.put("18-25", 0L);
            ageGroups.put("26-35", 0L);
            ageGroups.put("36-50", 0L);
            ageGroups.put("51+", 0L);

            for (Registration reg : event.getRegistrations()) {
                User user = reg.getUser();
                if (user != null && user.getUserprofile() != null) {
                    LocalDate dob = user.getUserprofile().getDateOfBirth();
                    if (dob != null) {
                        int age = Period.between(dob, LocalDate.now()).getYears();

                        if (age >= 18 && age <= 25) {
                            ageGroups.put("18-25", ageGroups.get("18-25") + 1);
                        } else if (age <= 35) {
                            ageGroups.put("26-35", ageGroups.get("26-35") + 1);
                        } else if (age <= 50) {
                            ageGroups.put("36-50", ageGroups.get("36-50") + 1);
                        } else {
                            ageGroups.put("51+", ageGroups.get("51+") + 1);
                        }
                    }
                }
            }

            AgeGroupStatsDTO dto = new AgeGroupStatsDTO();
            dto.setEventId(event.getId());
            dto.setEventTitle(event.getTitle());
            dto.setAgeGroups(ageGroups);

            allResults.add(dto);
        }

        // Pagination manually
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allResults.size());
        List<AgeGroupStatsDTO> pagedList = allResults.subList(start, end);

        return new PageImpl<>(pagedList, pageable, allResults.size());
    }

}
