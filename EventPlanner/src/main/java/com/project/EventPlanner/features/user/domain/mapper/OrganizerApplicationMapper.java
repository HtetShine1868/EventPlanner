package com.project.EventPlanner.features.user.domain.mapper;

import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel ="spring")
public interface OrganizerApplicationMapper {

    @Mapping(source = "user.id", target = "userId")
    OrganizerApplicationDTO toDTO(OrganizerApplication application);

    @Mapping(source = "userId", target = "user.id")
    OrganizerApplication toEntity(OrganizerApplicationDTO dto);

    List<OrganizerApplicationDTO> toDTOList(List<OrganizerApplication> apps);
}
