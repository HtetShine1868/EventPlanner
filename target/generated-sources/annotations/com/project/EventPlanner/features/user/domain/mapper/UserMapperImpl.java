package com.project.EventPlanner.features.user.domain.mapper;

import com.project.EventPlanner.features.user.domain.dto.UserRegisterDto;
import com.project.EventPlanner.features.user.domain.dto.UserResponseDto;
import com.project.EventPlanner.features.user.domain.model.Role;
import com.project.EventPlanner.features.user.domain.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-23T12:20:30+0630",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserRegisterDto userRegisterDto) {
        if ( userRegisterDto == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( userRegisterDto.getUsername() );
        user.setEmail( userRegisterDto.getEmail() );
        user.setPassword( userRegisterDto.getPassword() );

        return user;
    }

    @Override
    public UserResponseDto toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setRoleName( userRoleName( user ) );
        userResponseDto.setId( user.getId() );
        userResponseDto.setUsername( user.getUsername() );
        userResponseDto.setEmail( user.getEmail() );

        return userResponseDto;
    }

    @Override
    public List<UserResponseDto> toDTOList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponseDto> list = new ArrayList<UserResponseDto>( users.size() );
        for ( User user : users ) {
            list.add( toDTO( user ) );
        }

        return list;
    }

    private String userRoleName(User user) {
        if ( user == null ) {
            return null;
        }
        Role role = user.getRole();
        if ( role == null ) {
            return null;
        }
        String name = role.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
