package com.project.EventPlanner.features.user.domain.mapper;

import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationRequestDTO;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import com.project.EventPlanner.features.user.domain.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-18T20:43:28+0630",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class OrganizerApplicationMapperImpl implements OrganizerApplicationMapper {

    @Override
    public OrganizerApplicationDTO toDTO(OrganizerApplication application) {
        if ( application == null ) {
            return null;
        }

        OrganizerApplicationDTO organizerApplicationDTO = new OrganizerApplicationDTO();

        organizerApplicationDTO.setUserId( applicationUserId( application ) );
        organizerApplicationDTO.setId( application.getId() );
        organizerApplicationDTO.setOrganizerName( application.getOrganizerName() );
        organizerApplicationDTO.setEmail( application.getEmail() );
        organizerApplicationDTO.setDescription( application.getDescription() );
        organizerApplicationDTO.setStatus( application.getStatus() );
        organizerApplicationDTO.setAppliedAt( application.getAppliedAt() );

        return organizerApplicationDTO;
    }

    @Override
    public OrganizerApplication toEntity(OrganizerApplicationRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        OrganizerApplication organizerApplication = new OrganizerApplication();

        organizerApplication.setOrganizerName( dto.getOrganizerName() );
        organizerApplication.setDescription( dto.getDescription() );
        organizerApplication.setEmail( dto.getEmail() );

        return organizerApplication;
    }

    @Override
    public List<OrganizerApplicationDTO> toDTOList(List<OrganizerApplication> applications) {
        if ( applications == null ) {
            return null;
        }

        List<OrganizerApplicationDTO> list = new ArrayList<OrganizerApplicationDTO>( applications.size() );
        for ( OrganizerApplication organizerApplication : applications ) {
            list.add( toDTO( organizerApplication ) );
        }

        return list;
    }

    private Long applicationUserId(OrganizerApplication organizerApplication) {
        if ( organizerApplication == null ) {
            return null;
        }
        User user = organizerApplication.getUser();
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
