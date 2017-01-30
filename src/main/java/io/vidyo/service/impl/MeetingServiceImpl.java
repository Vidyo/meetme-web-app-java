package io.vidyo.service.impl;

import io.vidyo.service.MeetingService;
import io.vidyo.domain.Meeting;
import io.vidyo.repository.MeetingRepository;
import io.vidyo.service.dto.MeetingDTO;
import io.vidyo.service.mapper.MeetingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Meeting.
 */
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService{

    private final Logger log = LoggerFactory.getLogger(MeetingServiceImpl.class);

    @Inject
    private MeetingRepository meetingRepository;

    @Inject
    private MeetingMapper meetingMapper;

    /**
     * Save a meeting.
     *
     * @param meetingDTO the entity to save
     * @return the persisted entity
     */
    public MeetingDTO save(MeetingDTO meetingDTO) {
        log.debug("Request to save Meeting : {}", meetingDTO);
        Meeting meeting = meetingMapper.meetingDTOToMeeting(meetingDTO);
        meeting = meetingRepository.save(meeting);
        MeetingDTO result = meetingMapper.meetingToMeetingDTO(meeting);
        return result;
    }

    /**
     *  Get all the meetings.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MeetingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Meetings");
        Page<Meeting> result = meetingRepository.findByOwnerIsCurrentUser(pageable);
        return result.map(meeting -> meetingMapper.meetingToMeetingDTO(meeting));
    }

    /**
     *  Get one meeting by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MeetingDTO findOne(Long id) {
        log.debug("Request to get Meeting : {}", id);
        Meeting meeting = meetingRepository.findOne(id);
        MeetingDTO meetingDTO = meetingMapper.meetingToMeetingDTO(meeting);
        return meetingDTO;
    }

    @Transactional(readOnly = true)
    public Optional<MeetingDTO> findByKey(String key) {
        log.debug("Request to get Meeting : {}", key);
        Optional<Meeting> meeting = meetingRepository.findByKey(key);
        if (meeting.isPresent()) {
            Optional<MeetingDTO> meetingDTO = Optional.ofNullable(meetingMapper.meetingToMeetingDTO(meeting.get()));
            return  meetingDTO;
        }
        return Optional.empty();
    }

    /**
     *  Delete the  meeting by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Meeting : {}", id);
        meetingRepository.delete(id);
    }
}
