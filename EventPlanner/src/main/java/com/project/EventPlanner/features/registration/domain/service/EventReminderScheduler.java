package com.project.EventPlanner.features.registration.domain.service;

import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.registration.domain.repository.RegistrationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventReminderScheduler {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepo;
    private final EmailService emailService;

    public EventReminderScheduler(EventRepository eventRepository, RegistrationRepository registrationRepo, EmailService emailService) {
        this.eventRepository = eventRepository;
        this.registrationRepo = registrationRepo;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 8 * * ?") // every day at 8 AM
    public void sendReminders() {
        LocalDateTime tomorrowStart = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime tomorrowEnd = tomorrowStart.withHour(23).withMinute(59).withSecond(59);

        List<Event> events = eventRepository.findByStartTimeBetween(tomorrowStart, tomorrowEnd);

        for (Event event : events) {
            registrationRepo.findByEvent(event).forEach(reg -> {
                String subject = "Reminder: Event Tomorrow - " + event.getTitle();
                String text = "Hi " + reg.getUser().getUserprofile().getFullName() + ",\n\n" +
                        "This is a reminder that the event '" + event.getTitle() + "' is happening tomorrow.\n" +
                        "Time: " + event.getStartTime() + "\n" +
                        "Location: " + (event.getLocation() != null ? event.getLocation() : "Online") + "\n\n" +
                        "See you there!";
                emailService.sendEmail(reg.getUser().getEmail(), subject, text);
            });
        }
    }
}
