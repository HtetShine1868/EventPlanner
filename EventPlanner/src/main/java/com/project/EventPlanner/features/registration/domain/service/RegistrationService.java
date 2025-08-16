package com.project.EventPlanner.features.registration.domain.service;

import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.Mapper.EventMapper;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.dto.RegisteredUserDTO;
import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.registration.domain.mapper.RegisterMapper;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationRequestDTO;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationResponseDTO;
import com.project.EventPlanner.features.registration.domain.dto.EventAnalysisDTO;
import com.project.EventPlanner.features.registration.domain.model.Registration;
import com.project.EventPlanner.features.registration.domain.repository.RegistrationRepository;
import com.project.EventPlanner.features.user.domain.model.User;

import com.project.EventPlanner.features.user.domain.model.UserProfile;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepo;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RegisterMapper registerMapper;
    private final EventMapper eventMapper;


    public RegistrationResponseDTO registerUserToEvent(RegistrationRequestDTO dto, User currentUser) {
        // 1. Fetch Event
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + dto.getEventId()));

        // 2. Check Event Status
        if (event.getStatus() != EventStatus.APPROVED) {
            throw new RuntimeException("Event is not approved for registration");
        }

        // 3. Check Capacity
        if (event.getRegisteredCount() != null && event.getRegisteredCount() >= event.getCapacity()) {
            throw new RuntimeException("Event is full");
        }

        // 4. Use current logged-in user
        User user = currentUser;

        // 5. Prevent duplicate registration
        boolean alreadyRegistered = registrationRepo.existsByEventIdAndUserId(dto.getEventId(), user.getId());
        if (alreadyRegistered) {
            throw new RuntimeException("User already registered for this event");
        }

        // 6. Create and save registration
        Registration registration = new Registration();
        registration.setEvent(event);
        registration.setUser(user);
        registration.setRegisterAt(LocalDateTime.now());
        registrationRepo.save(registration);

        // 7. Update registered count in event
        int updatedCount = (event.getRegisteredCount() == null) ? 1 : event.getRegisteredCount() + 1;
        event.setRegisteredCount(updatedCount);
        eventRepository.save(event);

        // 8. Return response DTO
        return registerMapper.toDTO(registration);
    }
    public List<Registration> getAttendees(Long eventId) {

        return registrationRepo.findByEventId(eventId);
    }

    public Registration checkIn(Long eventId, Long userId) {
        Registration reg = registrationRepo.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        reg.setCheckedIn(true);
        return registrationRepo.save(reg);
    }

    public Page<EventResponseDto> getRegisteredEventsByUser(User user, Pageable pageable) {
        Page<Registration> registrations = registrationRepo.findByUserId(user.getId(), pageable);
        return registrations.map(reg -> eventMapper.toDto(reg.getEvent()));
    }


    public List<RegisteredUserDTO> getRegisteredUsersByEvent(Long eventId, User currentUser) {

        System.out.println("currentUser.getId(): " + currentUser.getId());
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        System.out.println("event.getCreatedBy().getId(): " + event.getCreatedBy().getId());



        if (event.getCreatedBy() == null) {
            throw new RuntimeException("❌ Event has no createdBy set!");
        }

        if (!event.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to view registrations for this event.");
        }

        List<Registration> registrations = registrationRepo.findByEventId(eventId);

        return registrations.stream()
                .map(reg -> {
                    User user = reg.getUser();
                    UserProfile profile = user.getUserprofile(); // Optional
                    RegisteredUserDTO dto = new RegisteredUserDTO();
                    dto.setUserId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setFullName(profile != null ? profile.getFullName() : null);
                    return dto;
                })
                .toList();
    }



    public EventAnalysisDTO getSimpleAnalysis(Long eventId) {
        long total = registrationRepo.countAttendeesByEventId(eventId);

        // Gender counts -> with percentages
        List<Object[]> genderRows = registrationRepo.genderCountsByEvent(eventId);
        List<EventAnalysisDTO.GenderStat> genderStats = new ArrayList<>();
        for (Object[] row : genderRows) {
            String gender = row[0] == null ? "UNKNOWN" : row[0].toString();
            long count = ((Number) row[1]).longValue();
            double pct = (total > 0) ? (count * 100.0 / total) : 0.0;
            genderStats.add(new EventAnalysisDTO.GenderStat(gender, count, pct));
        }

        // Age group counts -> pick the most common
        String mostCommonAgeGroup = "N/A";
        long maxCount = -1;
        List<Object[]> ageRows = registrationRepo.ageGroupCountsByEvent(eventId);
        for (Object[] row : ageRows) {
            String ageGroup = row[0] == null ? "UNKNOWN" : row[0].toString();
            long count = ((Number) row[1]).longValue();
            if (count > maxCount) {
                maxCount = count;
                mostCommonAgeGroup = ageGroup;
            }
        }

        return new EventAnalysisDTO(eventId, total, genderStats, mostCommonAgeGroup);
    }
}
