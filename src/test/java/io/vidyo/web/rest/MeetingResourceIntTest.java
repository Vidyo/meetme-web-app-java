package io.vidyo.web.rest;

import io.vidyo.MeetmeApp;

import io.vidyo.domain.Meeting;
import io.vidyo.repository.MeetingRepository;
import io.vidyo.service.MeetingService;
import io.vidyo.service.dto.MeetingDTO;
import io.vidyo.service.mapper.MeetingMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MeetingResource REST controller.
 *
 * @see MeetingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetmeApp.class)
public class MeetingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    @Inject
    private MeetingRepository meetingRepository;

    @Inject
    private MeetingMapper meetingMapper;

    @Inject
    private MeetingService meetingService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMeetingMockMvc;

    private Meeting meeting;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MeetingResource meetingResource = new MeetingResource();
        ReflectionTestUtils.setField(meetingResource, "meetingService", meetingService);
        this.restMeetingMockMvc = MockMvcBuilders.standaloneSetup(meetingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meeting createEntity(EntityManager em) {
        Meeting meeting = new Meeting()
                .name(DEFAULT_NAME)
                .key(DEFAULT_KEY);
        return meeting;
    }

    @Before
    public void initTest() {
        meeting = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeeting() throws Exception {
        int databaseSizeBeforeCreate = meetingRepository.findAll().size();

        // Create the Meeting
        MeetingDTO meetingDTO = meetingMapper.meetingToMeetingDTO(meeting);

        restMeetingMockMvc.perform(post("/api/meetings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meetingDTO)))
            .andExpect(status().isCreated());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeCreate + 1);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMeeting.getKey()).isEqualTo(DEFAULT_KEY);
    }

    @Test
    @Transactional
    public void createMeetingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = meetingRepository.findAll().size();

        // Create the Meeting with an existing ID
        Meeting existingMeeting = new Meeting();
        existingMeeting.setId(1L);
        MeetingDTO existingMeetingDTO = meetingMapper.meetingToMeetingDTO(existingMeeting);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeetingMockMvc.perform(post("/api/meetings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingMeetingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = meetingRepository.findAll().size();
        // set the field null
        meeting.setName(null);

        // Create the Meeting, which fails.
        MeetingDTO meetingDTO = meetingMapper.meetingToMeetingDTO(meeting);

        restMeetingMockMvc.perform(post("/api/meetings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meetingDTO)))
            .andExpect(status().isBadRequest());

        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = meetingRepository.findAll().size();
        // set the field null
        meeting.setKey(null);

        // Create the Meeting, which fails.
        MeetingDTO meetingDTO = meetingMapper.meetingToMeetingDTO(meeting);

        restMeetingMockMvc.perform(post("/api/meetings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meetingDTO)))
            .andExpect(status().isBadRequest());

        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMeetings() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList
        restMeetingMockMvc.perform(get("/api/meetings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meeting.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())));
    }

    @Test
    @Transactional
    public void getMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get the meeting
        restMeetingMockMvc.perform(get("/api/meetings/{id}", meeting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(meeting.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMeeting() throws Exception {
        // Get the meeting
        restMeetingMockMvc.perform(get("/api/meetings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Update the meeting
        Meeting updatedMeeting = meetingRepository.findOne(meeting.getId());
        updatedMeeting
                .name(UPDATED_NAME)
                .key(UPDATED_KEY);
        MeetingDTO meetingDTO = meetingMapper.meetingToMeetingDTO(updatedMeeting);

        restMeetingMockMvc.perform(put("/api/meetings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meetingDTO)))
            .andExpect(status().isOk());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeeting.getKey()).isEqualTo(UPDATED_KEY);
    }

    @Test
    @Transactional
    public void updateNonExistingMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Create the Meeting
        MeetingDTO meetingDTO = meetingMapper.meetingToMeetingDTO(meeting);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMeetingMockMvc.perform(put("/api/meetings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meetingDTO)))
            .andExpect(status().isCreated());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);
        int databaseSizeBeforeDelete = meetingRepository.findAll().size();

        // Get the meeting
        restMeetingMockMvc.perform(delete("/api/meetings/{id}", meeting.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
