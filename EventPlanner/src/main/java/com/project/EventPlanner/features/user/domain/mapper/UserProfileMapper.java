package com.project.EventPlanner.features.user.domain.mapper;

import com.project.EventPlanner.features.user.domain.dto.UserProfileRequestDTO;
import com.project.EventPlanner.features.user.domain.dto.UserProfileResponseDTO;
import com.project.EventPlanner.features.user.domain.model.UserProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfile toEntity(UserProfileRequestDTO dto);

    @Mapping(source = "user.id", target = "userId")
    UserProfileResponseDTO toDto(UserProfile profile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserProfileFromDto(UserProfileRequestDTO dto, @MappingTarget UserProfile profile);
}