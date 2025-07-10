package com.project.EventPlanner.features.user.domain.mapper;

import com.project.EventPlanner.features.user.domain.dto.UserRegisterDto;
import com.project.EventPlanner.features.user.domain.dto.UserResponseDto;
import com.project.EventPlanner.features.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel ="spring")
public interface UserMapper {
     User toEntity(UserRegisterDto userRegisterDto);
    @Mapping(source = "role.name", target = "roleName")
    UserResponseDto toDTO(User user);
    List<UserResponseDto> toDTOList(List<User> users);

}
