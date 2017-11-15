package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.ChallengeSubmission;
import fi.lifesup.hackathon.repository.ChallengeSubmissionRepository;

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

/**
 * Test class for the ChallengeSubmissionResource REST controller.
 *
 * @see ChallengeSubmissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class ChallengeSubmissionResourceIntTest {

    private static final Long DEFAULT_APPLICATION_ID = 1L;
    private static final Long UPDATED_APPLICATION_ID = 2L;

    private static final String DEFAULT_FILE_PATH = "AAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBB";

    private static final String DEFAULT_ADDITIONAL_NOTE = "AAAAA";
    private static final String UPDATED_ADDITIONAL_NOTE = "BBBBB";

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFIED_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_MODIFIED_DATE);

    private static final String DEFAULT_MODIFIED_BY = "AAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBB";

    @Inject
    private ChallengeSubmissionRepository challengeSubmissionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChallengeSubmissionMockMvc;

    private ChallengeSubmission challengeSubmission;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChallengeSubmissionResource challengeSubmissionResource = new ChallengeSubmissionResource();
        ReflectionTestUtils.setField(challengeSubmissionResource, "challengeSubmissionRepository", challengeSubmissionRepository);
        this.restChallengeSubmissionMockMvc = MockMvcBuilders.standaloneSetup(challengeSubmissionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChallengeSubmission createEntity(EntityManager em) {
        ChallengeSubmission challengeSubmission = new ChallengeSubmission()
                .applicationId(DEFAULT_APPLICATION_ID)
                .filePath(DEFAULT_FILE_PATH)
                .additionalNote(DEFAULT_ADDITIONAL_NOTE)
                .modifiedDate(DEFAULT_MODIFIED_DATE)
                .modifiedBy(DEFAULT_MODIFIED_BY);
        return challengeSubmission;
    }

    @Before
    public void initTest() {
        challengeSubmission = createEntity(em);
    }

    @Test
    @Transactional
    public void createChallengeSubmission() throws Exception {
        int databaseSizeBeforeCreate = challengeSubmissionRepository.findAll().size();

        // Create the ChallengeSubmission

        restChallengeSubmissionMockMvc.perform(post("/api/challenge-submissions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeSubmission)))
                .andExpect(status().isCreated());

        // Validate the ChallengeSubmission in the database
        List<ChallengeSubmission> challengeSubmissions = challengeSubmissionRepository.findAll();
        assertThat(challengeSubmissions).hasSize(databaseSizeBeforeCreate + 1);
        ChallengeSubmission testChallengeSubmission = challengeSubmissions.get(challengeSubmissions.size() - 1);
        assertThat(testChallengeSubmission.getApplicationId()).isEqualTo(DEFAULT_APPLICATION_ID);
        assertThat(testChallengeSubmission.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testChallengeSubmission.getAdditionalNote()).isEqualTo(DEFAULT_ADDITIONAL_NOTE);
        assertThat(testChallengeSubmission.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testChallengeSubmission.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void checkApplicationIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeSubmissionRepository.findAll().size();
        // set the field null
        challengeSubmission.setApplicationId(null);

        // Create the ChallengeSubmission, which fails.

        restChallengeSubmissionMockMvc.perform(post("/api/challenge-submissions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeSubmission)))
                .andExpect(status().isBadRequest());

        List<ChallengeSubmission> challengeSubmissions = challengeSubmissionRepository.findAll();
        assertThat(challengeSubmissions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChallengeSubmissions() throws Exception {
        // Initialize the database
        challengeSubmissionRepository.saveAndFlush(challengeSubmission);

        // Get all the challengeSubmissions
        restChallengeSubmissionMockMvc.perform(get("/api/challenge-submissions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(challengeSubmission.getId().intValue())))
                .andExpect(jsonPath("$.[*].applicationId").value(hasItem(DEFAULT_APPLICATION_ID.intValue())))
                .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH.toString())))
                .andExpect(jsonPath("$.[*].additionalNote").value(hasItem(DEFAULT_ADDITIONAL_NOTE.toString())))
                .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE_STR)))
                .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY.toString())));
    }

    @Test
    @Transactional
    public void getChallengeSubmission() throws Exception {
        // Initialize the database
        challengeSubmissionRepository.saveAndFlush(challengeSubmission);

        // Get the challengeSubmission
        restChallengeSubmissionMockMvc.perform(get("/api/challenge-submissions/{id}", challengeSubmission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(challengeSubmission.getId().intValue()))
            .andExpect(jsonPath("$.applicationId").value(DEFAULT_APPLICATION_ID.intValue()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH.toString()))
            .andExpect(jsonPath("$.additionalNote").value(DEFAULT_ADDITIONAL_NOTE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE_STR))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChallengeSubmission() throws Exception {
        // Get the challengeSubmission
        restChallengeSubmissionMockMvc.perform(get("/api/challenge-submissions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChallengeSubmission() throws Exception {
        // Initialize the database
        challengeSubmissionRepository.saveAndFlush(challengeSubmission);
        int databaseSizeBeforeUpdate = challengeSubmissionRepository.findAll().size();

        // Update the challengeSubmission
        ChallengeSubmission updatedChallengeSubmission = challengeSubmissionRepository.findOne(challengeSubmission.getId());
        updatedChallengeSubmission
                .applicationId(UPDATED_APPLICATION_ID)
                .filePath(UPDATED_FILE_PATH)
                .additionalNote(UPDATED_ADDITIONAL_NOTE)
                .modifiedDate(UPDATED_MODIFIED_DATE)
                .modifiedBy(UPDATED_MODIFIED_BY);

        restChallengeSubmissionMockMvc.perform(put("/api/challenge-submissions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChallengeSubmission)))
                .andExpect(status().isOk());

        // Validate the ChallengeSubmission in the database
        List<ChallengeSubmission> challengeSubmissions = challengeSubmissionRepository.findAll();
        assertThat(challengeSubmissions).hasSize(databaseSizeBeforeUpdate);
        ChallengeSubmission testChallengeSubmission = challengeSubmissions.get(challengeSubmissions.size() - 1);
        assertThat(testChallengeSubmission.getApplicationId()).isEqualTo(UPDATED_APPLICATION_ID);
        assertThat(testChallengeSubmission.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testChallengeSubmission.getAdditionalNote()).isEqualTo(UPDATED_ADDITIONAL_NOTE);
        assertThat(testChallengeSubmission.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testChallengeSubmission.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void deleteChallengeSubmission() throws Exception {
        // Initialize the database
        challengeSubmissionRepository.saveAndFlush(challengeSubmission);
        int databaseSizeBeforeDelete = challengeSubmissionRepository.findAll().size();

        // Get the challengeSubmission
        restChallengeSubmissionMockMvc.perform(delete("/api/challenge-submissions/{id}", challengeSubmission.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChallengeSubmission> challengeSubmissions = challengeSubmissionRepository.findAll();
        assertThat(challengeSubmissions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
