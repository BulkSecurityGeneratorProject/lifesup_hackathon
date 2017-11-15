package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.ChallengeSubmissionFeedback;
import fi.lifesup.hackathon.repository.ChallengeSubmissionFeedbackRepository;

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
 * Test class for the ChallengeSubmissionFeedbackResource REST controller.
 *
 * @see ChallengeSubmissionFeedbackResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class ChallengeSubmissionFeedbackResourceIntTest {

    private static final String DEFAULT_FEEDBACK_TEXT = "AAAAA";
    private static final String UPDATED_FEEDBACK_TEXT = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CREATED_DATE);

    private static final String DEFAULT_CREATED_BY = "AAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBB";

    @Inject
    private ChallengeSubmissionFeedbackRepository challengeSubmissionFeedbackRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChallengeSubmissionFeedbackMockMvc;

    private ChallengeSubmissionFeedback challengeSubmissionFeedback;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChallengeSubmissionFeedbackResource challengeSubmissionFeedbackResource = new ChallengeSubmissionFeedbackResource();
        ReflectionTestUtils.setField(challengeSubmissionFeedbackResource, "challengeSubmissionFeedbackRepository", challengeSubmissionFeedbackRepository);
        this.restChallengeSubmissionFeedbackMockMvc = MockMvcBuilders.standaloneSetup(challengeSubmissionFeedbackResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChallengeSubmissionFeedback createEntity(EntityManager em) {
        ChallengeSubmissionFeedback challengeSubmissionFeedback = new ChallengeSubmissionFeedback()
                .feedbackText(DEFAULT_FEEDBACK_TEXT)
                .createdDate(DEFAULT_CREATED_DATE)
                .createdBy(DEFAULT_CREATED_BY);
        return challengeSubmissionFeedback;
    }

    @Before
    public void initTest() {
        challengeSubmissionFeedback = createEntity(em);
    }

    @Test
    @Transactional
    public void createChallengeSubmissionFeedback() throws Exception {
        int databaseSizeBeforeCreate = challengeSubmissionFeedbackRepository.findAll().size();

        // Create the ChallengeSubmissionFeedback

        restChallengeSubmissionFeedbackMockMvc.perform(post("/api/challenge-submission-feedbacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeSubmissionFeedback)))
                .andExpect(status().isCreated());

        // Validate the ChallengeSubmissionFeedback in the database
        List<ChallengeSubmissionFeedback> challengeSubmissionFeedbacks = challengeSubmissionFeedbackRepository.findAll();
        assertThat(challengeSubmissionFeedbacks).hasSize(databaseSizeBeforeCreate + 1);
        ChallengeSubmissionFeedback testChallengeSubmissionFeedback = challengeSubmissionFeedbacks.get(challengeSubmissionFeedbacks.size() - 1);
        assertThat(testChallengeSubmissionFeedback.getFeedbackText()).isEqualTo(DEFAULT_FEEDBACK_TEXT);
        assertThat(testChallengeSubmissionFeedback.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testChallengeSubmissionFeedback.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllChallengeSubmissionFeedbacks() throws Exception {
        // Initialize the database
        challengeSubmissionFeedbackRepository.saveAndFlush(challengeSubmissionFeedback);

        // Get all the challengeSubmissionFeedbacks
        restChallengeSubmissionFeedbackMockMvc.perform(get("/api/challenge-submission-feedbacks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(challengeSubmissionFeedback.getId().intValue())))
                .andExpect(jsonPath("$.[*].feedbackText").value(hasItem(DEFAULT_FEEDBACK_TEXT.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getChallengeSubmissionFeedback() throws Exception {
        // Initialize the database
        challengeSubmissionFeedbackRepository.saveAndFlush(challengeSubmissionFeedback);

        // Get the challengeSubmissionFeedback
        restChallengeSubmissionFeedbackMockMvc.perform(get("/api/challenge-submission-feedbacks/{id}", challengeSubmissionFeedback.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(challengeSubmissionFeedback.getId().intValue()))
            .andExpect(jsonPath("$.feedbackText").value(DEFAULT_FEEDBACK_TEXT.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChallengeSubmissionFeedback() throws Exception {
        // Get the challengeSubmissionFeedback
        restChallengeSubmissionFeedbackMockMvc.perform(get("/api/challenge-submission-feedbacks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChallengeSubmissionFeedback() throws Exception {
        // Initialize the database
        challengeSubmissionFeedbackRepository.saveAndFlush(challengeSubmissionFeedback);
        int databaseSizeBeforeUpdate = challengeSubmissionFeedbackRepository.findAll().size();

        // Update the challengeSubmissionFeedback
        ChallengeSubmissionFeedback updatedChallengeSubmissionFeedback = challengeSubmissionFeedbackRepository.findOne(challengeSubmissionFeedback.getId());
        updatedChallengeSubmissionFeedback
                .feedbackText(UPDATED_FEEDBACK_TEXT)
                .createdDate(UPDATED_CREATED_DATE)
                .createdBy(UPDATED_CREATED_BY);

        restChallengeSubmissionFeedbackMockMvc.perform(put("/api/challenge-submission-feedbacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChallengeSubmissionFeedback)))
                .andExpect(status().isOk());

        // Validate the ChallengeSubmissionFeedback in the database
        List<ChallengeSubmissionFeedback> challengeSubmissionFeedbacks = challengeSubmissionFeedbackRepository.findAll();
        assertThat(challengeSubmissionFeedbacks).hasSize(databaseSizeBeforeUpdate);
        ChallengeSubmissionFeedback testChallengeSubmissionFeedback = challengeSubmissionFeedbacks.get(challengeSubmissionFeedbacks.size() - 1);
        assertThat(testChallengeSubmissionFeedback.getFeedbackText()).isEqualTo(UPDATED_FEEDBACK_TEXT);
        assertThat(testChallengeSubmissionFeedback.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testChallengeSubmissionFeedback.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void deleteChallengeSubmissionFeedback() throws Exception {
        // Initialize the database
        challengeSubmissionFeedbackRepository.saveAndFlush(challengeSubmissionFeedback);
        int databaseSizeBeforeDelete = challengeSubmissionFeedbackRepository.findAll().size();

        // Get the challengeSubmissionFeedback
        restChallengeSubmissionFeedbackMockMvc.perform(delete("/api/challenge-submission-feedbacks/{id}", challengeSubmissionFeedback.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChallengeSubmissionFeedback> challengeSubmissionFeedbacks = challengeSubmissionFeedbackRepository.findAll();
        assertThat(challengeSubmissionFeedbacks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
