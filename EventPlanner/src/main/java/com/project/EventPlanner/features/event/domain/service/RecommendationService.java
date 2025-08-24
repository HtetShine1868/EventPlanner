package com.project.EventPlanner.features.event.domain.service;


import com.project.EventPlanner.features.event.domain.dto.EventDTO;
import com.project.EventPlanner.features.event.domain.dto.EventRequestDto;
import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.model.EventCategory;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.registration.domain.model.Registration;
import com.project.EventPlanner.features.registration.domain.repository.RegistrationRepository;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import org.springframework.data.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public List<EventDTO> recommendEvents(Long userId, int limit) {
        // 1️⃣ Get user's registered events
        List<Registration> registrations = registrationRepository.findByUserId(userId);

        Set<Long> registeredEventIds = new HashSet<>();
        Set<EventCategory> userCategories = new HashSet<>();

        for (Registration reg : registrations) {
            Event event = reg.getEvent();
            if (event != null) {
                registeredEventIds.add(event.getId());
                if (event.getCategory() != null) userCategories.add(event.getCategory());
            }
        }

        // 2️⃣ Get upcoming events not registered yet
        LocalDateTime now = LocalDateTime.now();
        List<Event> upcomingEvents = eventRepository.findByStartTimeAfter(now)
                .stream()
                .filter(e -> !registeredEventIds.contains(e.getId()))
                .collect(Collectors.toList());

        // 3️⃣ Score events (category matches higher weight)
        List<EventScore> scoredEvents = new ArrayList<>();
        for (Event e : upcomingEvents) {
            int score = 0;

            // Category match = +10
            if (e.getCategory() != null && userCategories.contains(e.getCategory())) score += 10;

            // Description keyword match = +1 per word match
            if (e.getDescription() != null) {
                Set<String> eventWords = new HashSet<>(Arrays.asList(e.getDescription().toLowerCase().split("\\s+")));
                for (EventCategory cat : userCategories) {
                    if (eventWords.contains(cat.getName().toLowerCase())) score += 1;
                }
            }

            scoredEvents.add(new EventScore(e, score));
        }

        // 4️⃣ Sort descending by score
        List<EventDTO> recommendations = scoredEvents.stream()
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .map(es -> {
                    Event e = es.getEvent();
                    return new EventDTO(
                            e.getId(),
                            e.getTitle(),
                            e.getDescription(),
                            e.getLocation(),
                            e.getStartTime(),
                            e.getEndTime(),
                            e.getCategory() != null ? e.getCategory().getName() : null,
                            e.getOrganizer() != null ? e.getOrganizer().getUsername() : null
                    );
                })
                .limit(limit)
                .collect(Collectors.toList());

        return recommendations;
    }

    // Helper class to store score
    private static class EventScore {
        private final Event event;
        private final int score;

        public EventScore(Event event, int score) {
            this.event = event;
            this.score = score;
        }

        public Event getEvent() { return event; }
        public int getScore() { return score; }
    }
}