package com.project.EventPlanner.features.registration.api;

import com.project.EventPlanner.features.registration.domain.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;
    @PostMapping("/{eventId}/register")
    public ResponseEntity<String> register(@PathVariable Long eventId, @RequestParam Long userId) {
        registrationService.registerUserToEvent(eventId, userId);
        return ResponseEntity.ok("Registered successfully");
    }
}
