package com.project.EventPlanner.features.user.domain.mapper;


import com.project.EventPlanner.features.user.domain.dto.UserProfileDto;
import com.project.EventPlanner.features.user.domain.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(source = "user.id", target = "userId")
    UserProfileDto toDto(UserProfile userProfile);


    @Mapping(source = "userId", target = "user.id")
    UserProfile toEntity(UserProfileDto userProfileDto);

    List<UserProfileDto> toDTOList(List<UserProfile> profiles);

}
