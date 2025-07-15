package com.project.EventPlanner.features.registration.api;

import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationRequestDTO;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationResponseDTO;
import com.project.EventPlanner.features.registration.domain.model.Registration;
import com.project.EventPlanner.features.registration.domain.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Page<EventResponseDto>> getRegisteredEvents(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("registerAt").descending());
        Page<EventResponseDto> events = registrationService.getRegisteredEventsByUser(userId, pageable);
        return ResponseEntity.ok(events);
    }
}
