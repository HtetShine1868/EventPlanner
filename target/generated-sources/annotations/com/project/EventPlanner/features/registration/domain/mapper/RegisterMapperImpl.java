package com.project.EventPlanner.features.registration.domain.mapper;

import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationResponseDTO;
import com.project.EventPlanner.features.registration.domain.model.Registration;
import com.project.EventPlanner.features.user.domain.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-16T00:09:28+0630",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class RegisterMapperImpl implements RegisterMapper {

    @Override
    public RegistrationResponseDTO toDTO(Registration registration) {
        if ( registration == null ) {
            return null;
        }

        RegistrationResponseDTO registrationResponseDTO = new RegistrationResponseDTO();

        registrationResponseDTO.setEventTitle( registrationEventTitle( registration ) );
        registrationResponseDTO.setUsername( registrationUserUsername( registration ) );
        registrationResponseDTO.setRegisteredAt( registration.getRegisterAt() );
        registrationResponseDTO.setId( registration.getId() );

        return registrationResponseDTO;
    }

    private String registrationEventTitle(Registration registration) {
        if ( registration == null ) {
            return null;
        }
        Event event = registration.getEvent();
        if ( event == null ) {
            return null;
        }
        String title = event.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private String registrationUserUsername(Registration registration) {
        if ( registration == null ) {
            return null;
        }
        User user = registration.getUser();
        if ( user == null ) {
            return null;
        }
        String username = user.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
