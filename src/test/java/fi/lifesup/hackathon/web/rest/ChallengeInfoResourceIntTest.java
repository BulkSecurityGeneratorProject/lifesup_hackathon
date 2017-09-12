package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.ChallengeInfo;
import fi.lifesup.hackathon.repository.ChallengeInfoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fi.lifesup.hackathon.domain.enumeration.ChallengeStatus;
/**
 * Test class for the ChallengeInfoResource REST controller.
 *
 * @see ChallengeInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class ChallengeInfoResourceIntTest {

    private static final ZonedDateTime DEFAULT_EVENT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_EVENT_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_EVENT_START_TIME_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_EVENT_START_TIME);

    private static final ZonedDateTime DEFAULT_EVENT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_EVENT_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_EVENT_END_TIME_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_EVENT_END_TIME);

    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    private static final ChallengeStatus DEFAULT_STATUS = ChallengeStatus.DRAFT;
    private static final ChallengeStatus UPDATED_STATUS = ChallengeStatus.ACTIVE;

    private static final String DEFAULT_PRIZE = "AAAAA";
    private static final String UPDATED_PRIZE = "BBBBB";

    @Inject
    private ChallengeInfoRepository challengeInfoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChallengeInfoMockMvc;

    private ChallengeInfo challengeInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChallengeInfoResource challengeInfoResource = new ChallengeInfoResource();
        ReflectionTestUtils.setField(challengeInfoResource, "challengeInfoRepository", challengeInfoRepository);
        this.restChallengeInfoMockMvc = MockMvcBuilders.standaloneSetup(challengeInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChallengeInfo createEntity(EntityManager em) {
        ChallengeInfo challengeInfo = new ChallengeInfo()
                .eventStartTime(DEFAULT_EVENT_START_TIME)
                .eventEndTime(DEFAULT_EVENT_END_TIME)
                .location(DEFAULT_LOCATION)
                .status(DEFAULT_STATUS)
                .prize(DEFAULT_PRIZE);
        return challengeInfo;
    }

    @Before
    public void initTest() {
        challengeInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createChallengeInfo() throws Exception {
        int databaseSizeBeforeCreate = challengeInfoRepository.findAll().size();

        // Create the ChallengeInfo

        restChallengeInfoMockMvc.perform(post("/api/challenge-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeInfo)))
                .andExpect(status().isCreated());

        // Validate the ChallengeInfo in the database
        List<ChallengeInfo> challengeInfos = challengeInfoRepository.findAll();
        assertThat(challengeInfos).hasSize(databaseSizeBeforeCreate + 1);
        ChallengeInfo testChallengeInfo = challengeInfos.get(challengeInfos.size() - 1);
        assertThat(testChallengeInfo.getEventStartTime()).isEqualTo(DEFAULT_EVENT_START_TIME);
        assertThat(testChallengeInfo.getEventEndTime()).isEqualTo(DEFAULT_EVENT_END_TIME);
        assertThat(testChallengeInfo.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testChallengeInfo.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testChallengeInfo.getPrize()).isEqualTo(DEFAULT_PRIZE);
    }

    @Test
    @Transactional
    public void checkEventStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeInfoRepository.findAll().size();
        // set the field null
        challengeInfo.setEventStartTime(null);

        // Create the ChallengeInfo, which fails.

        restChallengeInfoMockMvc.perform(post("/api/challenge-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeInfo)))
                .andExpect(status().isBadRequest());

        List<ChallengeInfo> challengeInfos = challengeInfoRepository.findAll();
        assertThat(challengeInfos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEventEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeInfoRepository.findAll().size();
        // set the field null
        challengeInfo.setEventEndTime(null);

        // Create the ChallengeInfo, which fails.

        restChallengeInfoMockMvc.perform(post("/api/challenge-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeInfo)))
                .andExpect(status().isBadRequest());

        List<ChallengeInfo> challengeInfos = challengeInfoRepository.findAll();
        assertThat(challengeInfos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeInfoRepository.findAll().size();
        // set the field null
        challengeInfo.setLocation(null);

        // Create the ChallengeInfo, which fails.

        restChallengeInfoMockMvc.perform(post("/api/challenge-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeInfo)))
                .andExpect(status().isBadRequest());

        List<ChallengeInfo> challengeInfos = challengeInfoRepository.findAll();
        assertThat(challengeInfos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeInfoRepository.findAll().size();
        // set the field null
        challengeInfo.setPrize(null);

        // Create the ChallengeInfo, which fails.

        restChallengeInfoMockMvc.perform(post("/api/challenge-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeInfo)))
                .andExpect(status().isBadRequest());

        List<ChallengeInfo> challengeInfos = challengeInfoRepository.findAll();
        assertThat(challengeInfos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChallengeInfos() throws Exception {
        // Initialize the database
        challengeInfoRepository.saveAndFlush(challengeInfo);

        // Get all the challengeInfos
        restChallengeInfoMockMvc.perform(get("/api/challenge-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(challengeInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].eventStartTime").value(hasItem(DEFAULT_EVENT_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].eventEndTime").value(hasItem(DEFAULT_EVENT_END_TIME_STR)))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].prize").value(hasItem(DEFAULT_PRIZE.toString())));
    }

    @Test
    @Transactional
    public void getChallengeInfo() throws Exception {
        // Initialize the database
        challengeInfoRepository.saveAndFlush(challengeInfo);

        // Get the challengeInfo
        restChallengeInfoMockMvc.perform(get("/api/challenge-infos/{id}", challengeInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(challengeInfo.getId().intValue()))
            .andExpect(jsonPath("$.eventStartTime").value(DEFAULT_EVENT_START_TIME_STR))
            .andExpect(jsonPath("$.eventEndTime").value(DEFAULT_EVENT_END_TIME_STR))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.prize").value(DEFAULT_PRIZE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChallengeInfo() throws Exception {
        // Get the challengeInfo
        restChallengeInfoMockMvc.perform(get("/api/challenge-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChallengeInfo() throws Exception {
        // Initialize the database
        challengeInfoRepository.saveAndFlush(challengeInfo);
        int databaseSizeBeforeUpdate = challengeInfoRepository.findAll().size();

        // Update the challengeInfo
        ChallengeInfo updatedChallengeInfo = challengeInfoRepository.findOne(challengeInfo.getId());
        updatedChallengeInfo
                .eventStartTime(UPDATED_EVENT_START_TIME)
                .eventEndTime(UPDATED_EVENT_END_TIME)
                .location(UPDATED_LOCATION)
                .status(UPDATED_STATUS)
                .prize(UPDATED_PRIZE);

        restChallengeInfoMockMvc.perform(put("/api/challenge-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChallengeInfo)))
                .andExpect(status().isOk());

        // Validate the ChallengeInfo in the database
        List<ChallengeInfo> challengeInfos = challengeInfoRepository.findAll();
        assertThat(challengeInfos).hasSize(databaseSizeBeforeUpdate);
        ChallengeInfo testChallengeInfo = challengeInfos.get(challengeInfos.size() - 1);
        assertThat(testChallengeInfo.getEventStartTime()).isEqualTo(UPDATED_EVENT_START_TIME);
        assertThat(testChallengeInfo.getEventEndTime()).isEqualTo(UPDATED_EVENT_END_TIME);
        assertThat(testChallengeInfo.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testChallengeInfo.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testChallengeInfo.getPrize()).isEqualTo(UPDATED_PRIZE);
    }

    @Test
    @Transactional
    public void deleteChallengeInfo() throws Exception {
        // Initialize the database
        challengeInfoRepository.saveAndFlush(challengeInfo);
        int databaseSizeBeforeDelete = challengeInfoRepository.findAll().size();

        // Get the challengeInfo
        restChallengeInfoMockMvc.perform(delete("/api/challenge-infos/{id}", challengeInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChallengeInfo> challengeInfos = challengeInfoRepository.findAll();
        assertThat(challengeInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
