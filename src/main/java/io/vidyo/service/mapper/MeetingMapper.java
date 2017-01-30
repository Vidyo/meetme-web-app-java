package io.vidyo.service.mapper;

import io.vidyo.domain.*;
import io.vidyo.service.dto.MeetingDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Meeting and its DTO MeetingDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface MeetingMapper {

    @Mapping(source = "app.id", target = "appId")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "app.name", target = "appName")
    @Mapping(source = "app.key", target = "appKey")
    MeetingDTO meetingToMeetingDTO(Meeting meeting);

    List<MeetingDTO> meetingsToMeetingDTOs(List<Meeting> meetings);

    @Mapping(source = "appId", target = "app")
    @Mapping(source = "ownerId", target = "owner")
    Meeting meetingDTOToMeeting(MeetingDTO meetingDTO);

    List<Meeting> meetingDTOsToMeetings(List<MeetingDTO> meetingDTOs);

    default Application applicationFromId(Long id) {
        if (id == null) {
            return null;
        }
        Application application = new Application();
        application.setId(id);
        return application;
    }
}
