package com.project.EventPlanner.features.registration.api;

import com.project.EventPlanner.features.registration.domain.dto.RegistrationRequestDTO;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationResponseDTO;
import com.project.EventPlanner.features.registration.domain.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
