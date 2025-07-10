package com.project.EventPlanner.features.event.domain.Mapper;

import com.project.EventPlanner.features.event.domain.dto.EventRequestDto;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target="category.id",source = "categoryId")
    @Mapping(target = "organizer.id", source = "organizerId")
    Event toEntity(EventRequestDto dto);


    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "organizerUsername", source = "organizer.username")
    EventResponseDto toDto(Event event);
}
