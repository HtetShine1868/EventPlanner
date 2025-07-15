package com.project.EventPlanner.features.registration.api;

import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationRequestDTO;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationResponseDTO;
import com.project.EventPlanner.features.registration.domain.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDTO> register(@RequestBody RegistrationRequestDTO dto) {
        RegistrationResponseDTO response = registrationService.registerUserToEvent(dto);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/users/{userId}/registrations")
    public ResponseEntity<List<EventResponseDto>> getUserRegistrations(@PathVariable Long userId) {
        List<EventResponseDto> events = registrationService.getEventsRegisteredByUser(userId);
        return ResponseEntity.ok(events);
    }

}
