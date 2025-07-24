package com.project.EventPlanner.features.event.domain.Mapper;

import com.project.EventPlanner.features.event.domain.dto.CategoryDto;
import org.mapstruct.Mapper;
import com.project.EventPlanner.features.event.domain.model.EventCategory;
import com.project.EventPlanner.features.event.domain.dto.CategoryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto ToDto(EventCategory entity);
    EventCategory ToEntity(CategoryDto dto);
    List<CategoryDto> ToDtoList(List<EventCategory> entities);
}
