package io.vidyo.service.mapper;

import io.vidyo.domain.*;
import io.vidyo.service.dto.ApplicationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Application and its DTO ApplicationDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface ApplicationMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    ApplicationDTO applicationToApplicationDTO(Application application);

    List<ApplicationDTO> applicationsToApplicationDTOs(List<Application> applications);

    @Mapping(source = "ownerId", target = "owner")
    Application applicationDTOToApplication(ApplicationDTO applicationDTO);

    List<Application> applicationDTOsToApplications(List<ApplicationDTO> applicationDTOs);
}
