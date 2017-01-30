package io.vidyo.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.vidyo.service.MeetingService;
import io.vidyo.service.UserService;
import io.vidyo.service.util.RandomUtil;
import io.vidyo.web.rest.util.HeaderUtil;
import io.vidyo.web.rest.util.PaginationUtil;
import io.vidyo.service.dto.MeetingDTO;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Meeting.
 */
@RestController
@RequestMapping("/api")
public class MeetingResource {

    private final Logger log = LoggerFactory.getLogger(MeetingResource.class);

    @Inject
    private MeetingService meetingService;

    @Inject
    private UserService userService;

    /**
     * POST  /meetings : Create a new meeting.
     *
     * @param meetingDTO the meetingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new meetingDTO, or with status 400 (Bad Request) if the meeting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/meetings")
    @Timed
    public ResponseEntity<MeetingDTO> createMeeting(@Valid @RequestBody MeetingDTO meetingDTO) throws URISyntaxException {
        log.debug("REST request to save Meeting : {}", meetingDTO);
        if (meetingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("meeting", "idexists", "A new meeting cannot already have an ID")).body(null);
        }
        meetingDTO.setKey(RandomUtil.generateRandomAlphaNumeric(8));
        meetingDTO.setOwnerId(userService.getUserWithAuthorities().getId());
        MeetingDTO result = meetingService.save(meetingDTO);
        return ResponseEntity.created(new URI("/api/meetings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("meeting", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /meetings : Updates an existing meeting.
     *
     * @param meetingDTO the meetingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated meetingDTO,
     * or with status 400 (Bad Request) if the meetingDTO is not valid,
     * or with status 500 (Internal Server Error) if the meetingDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/meetings")
    @Timed
    public ResponseEntity<MeetingDTO> updateMeeting(@Valid @RequestBody MeetingDTO meetingDTO) throws URISyntaxException {
        log.debug("REST request to update Meeting : {}", meetingDTO);
        if (meetingDTO.getId() == null) {
            return createMeeting(meetingDTO);
        }

        // do not update room key on update
        MeetingDTO existing = meetingService.findOne(meetingDTO.getId());
        meetingDTO.setKey(existing.getKey());

        MeetingDTO result = meetingService.save(meetingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("meeting", meetingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /meetings : get all the meetings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of meetings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/meetings")
    @Timed
    public ResponseEntity<List<MeetingDTO>> getAllMeetings(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Meetings");
        Page<MeetingDTO> page = meetingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/meetings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /meetings/:id : get the "id" meeting.
     *
     * @param id the id of the meetingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the meetingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/meetings/{id}")
    @Timed
    public ResponseEntity<MeetingDTO> getMeeting(@PathVariable Long id) {
        log.debug("REST request to get Meeting : {}", id);
        MeetingDTO meetingDTO = meetingService.findOne(id);

        if (meetingDTO.getOwnerId().longValue() != userService.getUserWithAuthorities().getId().longValue()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return Optional.ofNullable(meetingDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /meetings/:id : delete the "id" meeting.
     *
     * @param id the id of the meetingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/meetings/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long id) {
        log.debug("REST request to delete Meeting : {}", id);

        MeetingDTO meetingDTO = meetingService.findOne(id);
        if (meetingDTO.getOwnerId().longValue() != userService.getUserWithAuthorities().getId().longValue()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        meetingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("meeting", id.toString())).build();
    }

}
