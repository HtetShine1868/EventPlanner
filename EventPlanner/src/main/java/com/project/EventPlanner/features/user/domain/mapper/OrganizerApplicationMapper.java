package com.project.EventPlanner.features.user.domain.mapper;

import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationRequestDTO;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface OrganizerApplicationMapper {

    @Mapping(source = "user.id", target = "userId")
    OrganizerApplicationDTO toDTO(OrganizerApplication application);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)          // ignore status from create dto
    @Mapping(target = "appliedAt", ignore = true)       // ignore appliedAt from create dto
    OrganizerApplication toEntity(OrganizerApplicationRequestDTO dto);

    List<OrganizerApplicationDTO> toDTOList(List<OrganizerApplication> applications);
}
