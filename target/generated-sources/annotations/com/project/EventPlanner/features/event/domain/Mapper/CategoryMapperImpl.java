package com.project.EventPlanner.features.event.domain.Mapper;

import com.project.EventPlanner.features.event.domain.dto.CategoryDto;
import com.project.EventPlanner.features.event.domain.model.EventCategory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-16T00:09:28+0630",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryDto ToDto(EventCategory entity) {
        if ( entity == null ) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId( entity.getId() );
        categoryDto.setName( entity.getName() );
        categoryDto.setDescription( entity.getDescription() );

        return categoryDto;
    }

    @Override
    public EventCategory ToEntity(CategoryDto dto) {
        if ( dto == null ) {
            return null;
        }

        EventCategory eventCategory = new EventCategory();

        eventCategory.setId( dto.getId() );
        eventCategory.setName( dto.getName() );
        eventCategory.setDescription( dto.getDescription() );

        return eventCategory;
    }

    @Override
    public List<CategoryDto> ToDtoList(List<EventCategory> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CategoryDto> list = new ArrayList<CategoryDto>( entities.size() );
        for ( EventCategory eventCategory : entities ) {
            list.add( ToDto( eventCategory ) );
        }

        return list;
    }
}
