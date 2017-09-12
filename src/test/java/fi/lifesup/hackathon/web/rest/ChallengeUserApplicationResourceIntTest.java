package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.ChallengeUserApplication;
import fi.lifesup.hackathon.repository.ChallengeUserApplicationRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ChallengeUserApplicationResource REST controller.
 *
 * @see ChallengeUserApplicationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class ChallengeUserApplicationResourceIntTest {

    private static final Long DEFAULT_CHALLENGE_ID = 1L;
    private static final Long UPDATED_CHALLENGE_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_APPLICATION_ID = 1L;
    private static final Long UPDATED_APPLICATION_ID = 2L;

    @Inject
    private ChallengeUserApplicationRepository challengeUserApplicationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChallengeUserApplicationMockMvc;

    private ChallengeUserApplication challengeUserApplication;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChallengeUserApplicationResource challengeUserApplicationResource = new ChallengeUserApplicationResource();
        ReflectionTestUtils.setField(challengeUserApplicationResource, "challengeUserApplicationRepository", challengeUserApplicationRepository);
        this.restChallengeUserApplicationMockMvc = MockMvcBuilders.standaloneSetup(challengeUserApplicationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChallengeUserApplication createEntity(EntityManager em) {
        ChallengeUserApplication challengeUserApplication = new ChallengeUserApplication()
                .challengeId(DEFAULT_CHALLENGE_ID)
                .userId(DEFAULT_USER_ID)
                .applicationId(DEFAULT_APPLICATION_ID);
        return challengeUserApplication;
    }

    @Before
    public void initTest() {
        challengeUserApplication = createEntity(em);
    }

    @Test
    @Transactional
    public void createChallengeUserApplication() throws Exception {
        int databaseSizeBeforeCreate = challengeUserApplicationRepository.findAll().size();

        // Create the ChallengeUserApplication

        restChallengeUserApplicationMockMvc.perform(post("/api/challenge-user-applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeUserApplication)))
                .andExpect(status().isCreated());

        // Validate the ChallengeUserApplication in the database
        List<ChallengeUserApplication> challengeUserApplications = challengeUserApplicationRepository.findAll();
        assertThat(challengeUserApplications).hasSize(databaseSizeBeforeCreate + 1);
        ChallengeUserApplication testChallengeUserApplication = challengeUserApplications.get(challengeUserApplications.size() - 1);
        assertThat(testChallengeUserApplication.getChallengeId()).isEqualTo(DEFAULT_CHALLENGE_ID);
        assertThat(testChallengeUserApplication.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testChallengeUserApplication.getApplicationId()).isEqualTo(DEFAULT_APPLICATION_ID);
    }

    @Test
    @Transactional
    public void getAllChallengeUserApplications() throws Exception {
        // Initialize the database
        challengeUserApplicationRepository.saveAndFlush(challengeUserApplication);

        // Get all the challengeUserApplications
        restChallengeUserApplicationMockMvc.perform(get("/api/challenge-user-applications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(challengeUserApplication.getId().intValue())))
                .andExpect(jsonPath("$.[*].challengeId").value(hasItem(DEFAULT_CHALLENGE_ID.intValue())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$.[*].applicationId").value(hasItem(DEFAULT_APPLICATION_ID.intValue())));
    }

    @Test
    @Transactional
    public void getChallengeUserApplication() throws Exception {
        // Initialize the database
        challengeUserApplicationRepository.saveAndFlush(challengeUserApplication);

        // Get the challengeUserApplication
        restChallengeUserApplicationMockMvc.perform(get("/api/challenge-user-applications/{id}", challengeUserApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(challengeUserApplication.getId().intValue()))
            .andExpect(jsonPath("$.challengeId").value(DEFAULT_CHALLENGE_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.applicationId").value(DEFAULT_APPLICATION_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingChallengeUserApplication() throws Exception {
        // Get the challengeUserApplication
        restChallengeUserApplicationMockMvc.perform(get("/api/challenge-user-applications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChallengeUserApplication() throws Exception {
        // Initialize the database
        challengeUserApplicationRepository.saveAndFlush(challengeUserApplication);
        int databaseSizeBeforeUpdate = challengeUserApplicationRepository.findAll().size();

        // Update the challengeUserApplication
        ChallengeUserApplication updatedChallengeUserApplication = challengeUserApplicationRepository.findOne(challengeUserApplication.getId());
        updatedChallengeUserApplication
                .challengeId(UPDATED_CHALLENGE_ID)
                .userId(UPDATED_USER_ID)
                .applicationId(UPDATED_APPLICATION_ID);

        restChallengeUserApplicationMockMvc.perform(put("/api/challenge-user-applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChallengeUserApplication)))
                .andExpect(status().isOk());

        // Validate the ChallengeUserApplication in the database
        List<ChallengeUserApplication> challengeUserApplications = challengeUserApplicationRepository.findAll();
        assertThat(challengeUserApplications).hasSize(databaseSizeBeforeUpdate);
        ChallengeUserApplication testChallengeUserApplication = challengeUserApplications.get(challengeUserApplications.size() - 1);
        assertThat(testChallengeUserApplication.getChallengeId()).isEqualTo(UPDATED_CHALLENGE_ID);
        assertThat(testChallengeUserApplication.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testChallengeUserApplication.getApplicationId()).isEqualTo(UPDATED_APPLICATION_ID);
    }

    @Test
    @Transactional
    public void deleteChallengeUserApplication() throws Exception {
        // Initialize the database
        challengeUserApplicationRepository.saveAndFlush(challengeUserApplication);
        int databaseSizeBeforeDelete = challengeUserApplicationRepository.findAll().size();

        // Get the challengeUserApplication
        restChallengeUserApplicationMockMvc.perform(delete("/api/challenge-user-applications/{id}", challengeUserApplication.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChallengeUserApplication> challengeUserApplications = challengeUserApplicationRepository.findAll();
        assertThat(challengeUserApplications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
