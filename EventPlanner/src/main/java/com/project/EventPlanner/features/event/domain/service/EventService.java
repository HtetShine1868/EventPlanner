package com.project.EventPlanner.features.event.domain.service;

import com.project.EventPlanner.common.enums.OrganizerApplicationStatus;
import com.project.EventPlanner.exception.EventConflictException;
import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.Mapper.EventMapper;
import com.project.EventPlanner.features.event.domain.dto.AgeGroupStatsDTO;
import com.project.EventPlanner.features.event.domain.dto.EventRequestDto;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.dto.GenderStatsDTO;
import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventCategoryRepository;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.registration.domain.model.Registration;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.OrganizerApplicationRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final ModelMapper modelMapper;
    private final OrganizerApplicationRepository organizerApplicationRepository;

    public EventResponseDto createEvent(EventRequestDto eventRequestDto,  User currentUser) {
        if (eventRequestDto.getEndTime().isBefore(eventRequestDto.getStartTime())) {
            throw new IllegalArgumentException("End time cannot be earlier than start time.");
        }
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


    public Page<EventResponseDto> getMyEvents(Long organizerId, String status, Long categoryId, String search, Pageable pageable) {
        Specification<Event> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Organizer filter
            predicates.add(cb.equal(root.get("organizer").get("id"), organizerId));

            // Status filter
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // Category filter
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            // Search filter (title)
            if (search != null && !search.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return eventRepository.findAll(spec, pageable)
                .map(EventResponseDto::fromEntity); // Map to DTO
    }



    public Page<EventResponseDto> filterEventsByCategoryAndLocation(Long categoryId, String location, Double latitude, Double longitude, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        EventStatus approved = EventStatus.APPROVED;
        Page<Event> filteredEvents;

        // Check if location is provided, do partial match (case-insensitive)
        boolean hasLocation = location != null && !location.isBlank();

        if (categoryId != null && hasLocation) {
            // Assuming your repo has a method with category, location partial match, status, and possibly location-based filtering
            filteredEvents = eventRepository.findByCategoryIdAndLocationContainingIgnoreCaseAndStatus(categoryId, location, approved, pageable);
        } else if (categoryId != null) {
            filteredEvents = eventRepository.findByCategoryIdAndStatus(categoryId, approved, pageable);
        } else if (hasLocation) {
            filteredEvents = eventRepository.findByLocationContainingIgnoreCaseAndStatus(location, approved, pageable);
        } else {
            filteredEvents = eventRepository.findByStatus(approved, pageable);
        }

        // If you want to filter by latitude/longitude radius, you'd typically do it here or inside a custom repo method.

        return filteredEvents.map(eventMapper::toDto);
    }

    public List<EventResponseDto> getTopTrendingEvents(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return eventRepository.findTopTrendingEvents(pageable)
                .stream().map(event -> modelMapper.map(event, EventResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<EventResponseDto> getTopTrendingEventsByCategory(Long categoryId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return eventRepository.findTopTrendingEventsByCategory(categoryId, pageable)
                .stream().map(event -> modelMapper.map(event, EventResponseDto.class))
                .collect(Collectors.toList());
    }

    public EventResponseDto getMostRegisteredEvent(Long organizerId) {
        Pageable limit = PageRequest.of(0, 1);
        List<Event> result = eventRepository.findTopEventByOrganizer(organizerId, limit);
        return result.isEmpty() ? null : eventMapper.toDto(result.get(0));
    }


    public Page<EventResponseDto> getEventsByApprovedOrganizer(Long organizerId, Pageable pageable) {
        // 1. Validate if user exists
        User user = userRepository.findById(organizerId)
                .orElseThrow(() -> new RuntimeException("User with ID " + organizerId + " does not exist."));

        // 2. Validate if user has applied as organizer
        OrganizerApplication app = organizerApplicationRepository.findByUserId(organizerId)
                .orElseThrow(() -> new RuntimeException("Organizer not found. This user has never applied as an organizer."));

        // 3. Check if organizer is approved
        if (!app.getStatus().equals(OrganizerApplicationStatus.APPROVED)) {
            throw new RuntimeException("Organizer is not approved. Access to events is not allowed.");
        }

        // 4. Fetch events
        return eventRepository.findByOrganizerId(organizerId, pageable)
                .map(EventResponseDto::fromEntity);
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

    public Page<GenderStatsDTO> getGenderStatsForOrganizer(Long organizerId, Long eventIdFilter, Long categoryIdFilter, Pageable pageable) {
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

        List<GenderStatsDTO> allResults = new ArrayList<>();

        for (Event event : events) {
            Map<String, Long> genderCounts = new LinkedHashMap<>();
            genderCounts.put("MALE", 0L);
            genderCounts.put("FEMALE", 0L);
            genderCounts.put("OTHER", 0L);

            for (Registration reg : event.getRegistrations()) {
                User user = reg.getUser();
                if (user != null && user.getUserprofile() != null && user.getUserprofile().getGender() != null) {
                    String gender = user.getUserprofile().getGender().name(); // Assuming enum
                    genderCounts.put(gender, genderCounts.getOrDefault(gender, 0L) + 1);
                }
            }

            GenderStatsDTO dto = new GenderStatsDTO();
            dto.setEventId(event.getId());
            dto.setEventTitle(event.getTitle());
            dto.setGenderCounts(genderCounts);

            allResults.add(dto);
        }

        // Apply pagination manually
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allResults.size());
        List<GenderStatsDTO> pagedList = allResults.subList(start, end);

        return new PageImpl<>(pagedList, pageable, allResults.size());
    }


}
