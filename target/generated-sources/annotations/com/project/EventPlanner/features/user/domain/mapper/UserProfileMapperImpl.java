package com.project.EventPlanner.features.user.domain.mapper;

import com.project.EventPlanner.features.user.domain.dto.UserProfileRequestDTO;
import com.project.EventPlanner.features.user.domain.dto.UserProfileResponseDTO;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.model.UserProfile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-18T01:07:15+0630",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class UserProfileMapperImpl implements UserProfileMapper {

    @Override
    public UserProfile toEntity(UserProfileRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserProfile.UserProfileBuilder userProfile = UserProfile.builder();

        userProfile.fullName( dto.getFullName() );
        userProfile.gender( dto.getGender() );
        userProfile.dateOfBirth( dto.getDateOfBirth() );
        userProfile.address( dto.getAddress() );

        return userProfile.build();
    }

    @Override
    public UserProfileResponseDTO toDto(UserProfile profile) {
        if ( profile == null ) {
            return null;
        }

        UserProfileResponseDTO.UserProfileResponseDTOBuilder userProfileResponseDTO = UserProfileResponseDTO.builder();

        userProfileResponseDTO.userId( profileUserId( profile ) );
        userProfileResponseDTO.id( profile.getId() );
        userProfileResponseDTO.fullName( profile.getFullName() );
        userProfileResponseDTO.gender( profile.getGender() );
        userProfileResponseDTO.dateOfBirth( profile.getDateOfBirth() );
        userProfileResponseDTO.address( profile.getAddress() );

        return userProfileResponseDTO.build();
    }

    @Override
    public void updateUserProfileFromDto(UserProfileRequestDTO dto, UserProfile profile) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getFullName() != null ) {
            profile.setFullName( dto.getFullName() );
        }
        if ( dto.getGender() != null ) {
            profile.setGender( dto.getGender() );
        }
        if ( dto.getDateOfBirth() != null ) {
            profile.setDateOfBirth( dto.getDateOfBirth() );
        }
        if ( dto.getAddress() != null ) {
            profile.setAddress( dto.getAddress() );
        }
    }

    private Long profileUserId(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }
        User user = userProfile.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
