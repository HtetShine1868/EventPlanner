package com.project.EventPlanner.features.registration.domain.service;

import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
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


    public Registration registerUserToEvent(Long eventId, Long userId) {
        // 1. Fetch Event
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));



        // 3. Check Capacity
        if (event.getRegisteredCount() >= event.getCapacity()) {
            throw new RuntimeException("Event is full");
        }

        // 4. Fetch User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 5. Prevent duplicate registration
        boolean alreadyRegistered = registrationRepo.existsByEventIdAndUserId(eventId, userId);
        if (alreadyRegistered) {
            throw new RuntimeException("You are already registered for this event");
        }

        // 6. Register the user
        Registration registration = new Registration();
        registration.setEvent(event);
        registration.setUser(user);
        registration.setRegisterAt(LocalDateTime.now());
        registrationRepo.save(registration);

        // 7. Update registered count in event
        event.setRegisteredCount(event.getRegisteredCount() + 1);
        eventRepository.save(event);

        return registration;
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

}
