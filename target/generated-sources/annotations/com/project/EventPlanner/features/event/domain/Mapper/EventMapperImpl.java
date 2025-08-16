package com.project.EventPlanner.features.event.domain.Mapper;

import com.project.EventPlanner.features.event.domain.dto.EventRequestDto;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.model.EventCategory;
import com.project.EventPlanner.features.user.domain.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-16T00:09:28+0630",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public Event toEntity(EventRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Event event = new Event();

        event.setCategory( eventRequestDtoToEventCategory( dto ) );
        event.setTitle( dto.getTitle() );
        event.setDescription( dto.getDescription() );
        event.setLocation( dto.getLocation() );
        event.setLatitude( dto.getLatitude() );
        event.setLongitude( dto.getLongitude() );
        event.setStartTime( dto.getStartTime() );
        event.setEndTime( dto.getEndTime() );
        event.setCapacity( dto.getCapacity() );
        event.setStatus( dto.getStatus() );

        return event;
    }

    @Override
    public EventResponseDto toDto(Event event) {
        if ( event == null ) {
            return null;
        }

        EventResponseDto.EventResponseDtoBuilder eventResponseDto = EventResponseDto.builder();

        eventResponseDto.categoryName( eventCategoryName( event ) );
        eventResponseDto.organizerUsername( eventOrganizerUsername( event ) );
        eventResponseDto.id( event.getId() );
        eventResponseDto.title( event.getTitle() );
        eventResponseDto.description( event.getDescription() );
        eventResponseDto.startTime( event.getStartTime() );
        eventResponseDto.endTime( event.getEndTime() );
        eventResponseDto.location( event.getLocation() );
        eventResponseDto.latitude( event.getLatitude() );
        eventResponseDto.longitude( event.getLongitude() );
        eventResponseDto.capacity( event.getCapacity() );
        eventResponseDto.status( event.getStatus() );

        eventResponseDto.registeredCount( event.getRegistrations() != null ? event.getRegistrations().size() : 0 );

        return eventResponseDto.build();
    }

    protected EventCategory eventRequestDtoToEventCategory(EventRequestDto eventRequestDto) {
        if ( eventRequestDto == null ) {
            return null;
        }

        EventCategory eventCategory = new EventCategory();

        eventCategory.setId( eventRequestDto.getCategoryId() );

        return eventCategory;
    }

    private String eventCategoryName(Event event) {
        if ( event == null ) {
            return null;
        }
        EventCategory category = event.getCategory();
        if ( category == null ) {
            return null;
        }
        String name = category.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String eventOrganizerUsername(Event event) {
        if ( event == null ) {
            return null;
        }
        User organizer = event.getOrganizer();
        if ( organizer == null ) {
            return null;
        }
        String username = organizer.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
