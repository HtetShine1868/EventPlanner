package com.project.EventPlanner.features.registration.domain;

import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationRequestDTO;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationResponseDTO;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationResponseDTO;
import com.project.EventPlanner.features.registration.domain.model.Registration;
import com.project.EventPlanner.features.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    @Mapping(target = "eventTitle", source = "event.title")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "registeredAt", source = "registerAt")
    RegistrationResponseDTO toDTO(Registration registration);
}
