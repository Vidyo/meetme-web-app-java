package io.vidyo.service;

import io.vidyo.service.dto.MeetingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Meeting.
 */
public interface MeetingService {

    /**
     * Save a meeting.
     *
     * @param meetingDTO the entity to save
     * @return the persisted entity
     */
    MeetingDTO save(MeetingDTO meetingDTO);

    /**
     *  Get all the meetings.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MeetingDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" meeting.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MeetingDTO findOne(Long id);

    Optional<MeetingDTO> findByKey(String key);

    /**
     *  Delete the "id" meeting.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
