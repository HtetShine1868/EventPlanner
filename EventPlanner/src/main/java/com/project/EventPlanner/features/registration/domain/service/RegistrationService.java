package com.project.EventPlanner.features.registration.domain.service;

import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.Mapper.EventMapper;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.registration.domain.RegisterMapper;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationRequestDTO;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationResponseDTO;
import com.project.EventPlanner.features.registration.domain.model.Registration;
import com.project.EventPlanner.features.registration.domain.repository.RegistrationRepository;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.event.domain.model.Event;

import com.project.EventPlanner.features.user.domain.repository.OrganizerApplicationRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepo;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RegisterMapper registerMapper;
    private final EventMapper eventMapper;


    public RegistrationResponseDTO registerUserToEvent(RegistrationRequestDTO dto) {
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

        // 4. Fetch User
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        // 5. Prevent duplicate registration
        boolean alreadyRegistered = registrationRepo.existsByEventIdAndUserId(dto.getEventId(), dto.getUserId());
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
    public List<EventResponseDto> getEventsRegisteredByUser(Long userId) {
        List<Registration> registrations = registrationRepo.findByUserId(userId);
        return registrations.stream()
                .map(reg -> eventMapper.toDto(reg.getEvent()))
                .toList();
    }

}
