package com.project.EventPlanner.features.event.domain.Mapper;

import com.project.EventPlanner.features.event.domain.dto.EventRequestDto;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    @Mapping(target = "capacity", source = "capacity")
    @Mapping(target = "category.id", source = "categoryId")
    Event toEntity(EventRequestDto dto);

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "organizerUsername", source = "organizer.username")
    @Mapping(target = "registeredCount", expression = "java(event.getRegistrations() != null ? event.getRegistrations().size() : 0)")
    EventResponseDto toDto(Event event);
}
