package com.project.EventPlanner.features.registration.domain.mapper;

import com.project.EventPlanner.features.registration.domain.dto.RegistrationResponseDTO;
import com.project.EventPlanner.features.registration.domain.model.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    @Mapping(target = "eventTitle", source = "event.title")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "registeredAt", source = "registerAt")
    RegistrationResponseDTO toDTO(Registration registration);
}
