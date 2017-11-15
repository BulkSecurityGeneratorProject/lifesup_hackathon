package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.ChallengeFeedback;
import fi.lifesup.hackathon.repository.ChallengeFeedbackRepository;

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
 * Test class for the ChallengeFeedbackResource REST controller.
 *
 * @see ChallengeFeedbackResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class ChallengeFeedbackResourceIntTest {

    private static final Long DEFAULT_APPLICATION_ID = 1L;
    private static final Long UPDATED_APPLICATION_ID = 2L;

    private static final String DEFAULT_FEEDBACK_FOR_US = "AAAAA";
    private static final String UPDATED_FEEDBACK_FOR_US = "BBBBB";

    private static final String DEFAULT_FEEDBACK_FOR_CHALLENGE = "AAAAA";
    private static final String UPDATED_FEEDBACK_FOR_CHALLENGE = "BBBBB";

    private static final String DEFAULT_PUBLIC_FEEDBACK = "AAAAA";
    private static final String UPDATED_PUBLIC_FEEDBACK = "BBBBB";

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    @Inject
    private ChallengeFeedbackRepository challengeFeedbackRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChallengeFeedbackMockMvc;

    private ChallengeFeedback challengeFeedback;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChallengeFeedbackResource challengeFeedbackResource = new ChallengeFeedbackResource();
        ReflectionTestUtils.setField(challengeFeedbackResource, "challengeFeedbackRepository", challengeFeedbackRepository);
        this.restChallengeFeedbackMockMvc = MockMvcBuilders.standaloneSetup(challengeFeedbackResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChallengeFeedback createEntity(EntityManager em) {
        ChallengeFeedback challengeFeedback = new ChallengeFeedback()
                .applicationId(DEFAULT_APPLICATION_ID)
                .feedbackForUs(DEFAULT_FEEDBACK_FOR_US)
                .feedbackForChallenge(DEFAULT_FEEDBACK_FOR_CHALLENGE)
                .publicFeedback(DEFAULT_PUBLIC_FEEDBACK)
                .rating(DEFAULT_RATING);
        return challengeFeedback;
    }

    @Before
    public void initTest() {
        challengeFeedback = createEntity(em);
    }

    @Test
    @Transactional
    public void createChallengeFeedback() throws Exception {
        int databaseSizeBeforeCreate = challengeFeedbackRepository.findAll().size();

        // Create the ChallengeFeedback

        restChallengeFeedbackMockMvc.perform(post("/api/challenge-feedbacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeFeedback)))
                .andExpect(status().isCreated());

        // Validate the ChallengeFeedback in the database
        List<ChallengeFeedback> challengeFeedbacks = challengeFeedbackRepository.findAll();
        assertThat(challengeFeedbacks).hasSize(databaseSizeBeforeCreate + 1);
        ChallengeFeedback testChallengeFeedback = challengeFeedbacks.get(challengeFeedbacks.size() - 1);
        assertThat(testChallengeFeedback.getApplicationId()).isEqualTo(DEFAULT_APPLICATION_ID);
        assertThat(testChallengeFeedback.getFeedbackForUs()).isEqualTo(DEFAULT_FEEDBACK_FOR_US);
        assertThat(testChallengeFeedback.getFeedbackForChallenge()).isEqualTo(DEFAULT_FEEDBACK_FOR_CHALLENGE);
        assertThat(testChallengeFeedback.getPublicFeedback()).isEqualTo(DEFAULT_PUBLIC_FEEDBACK);
        assertThat(testChallengeFeedback.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void checkApplicationIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeFeedbackRepository.findAll().size();
        // set the field null
        challengeFeedback.setApplicationId(null);

        // Create the ChallengeFeedback, which fails.

        restChallengeFeedbackMockMvc.perform(post("/api/challenge-feedbacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeFeedback)))
                .andExpect(status().isBadRequest());

        List<ChallengeFeedback> challengeFeedbacks = challengeFeedbackRepository.findAll();
        assertThat(challengeFeedbacks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeFeedbackRepository.findAll().size();
        // set the field null
        challengeFeedback.setRating(null);

        // Create the ChallengeFeedback, which fails.

        restChallengeFeedbackMockMvc.perform(post("/api/challenge-feedbacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeFeedback)))
                .andExpect(status().isBadRequest());

        List<ChallengeFeedback> challengeFeedbacks = challengeFeedbackRepository.findAll();
        assertThat(challengeFeedbacks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChallengeFeedbacks() throws Exception {
        // Initialize the database
        challengeFeedbackRepository.saveAndFlush(challengeFeedback);

        // Get all the challengeFeedbacks
        restChallengeFeedbackMockMvc.perform(get("/api/challenge-feedbacks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(challengeFeedback.getId().intValue())))
                .andExpect(jsonPath("$.[*].applicationId").value(hasItem(DEFAULT_APPLICATION_ID.intValue())))
                .andExpect(jsonPath("$.[*].feedbackForUs").value(hasItem(DEFAULT_FEEDBACK_FOR_US.toString())))
                .andExpect(jsonPath("$.[*].feedbackForChallenge").value(hasItem(DEFAULT_FEEDBACK_FOR_CHALLENGE.toString())))
                .andExpect(jsonPath("$.[*].publicFeedback").value(hasItem(DEFAULT_PUBLIC_FEEDBACK.toString())))
                .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));
    }

    @Test
    @Transactional
    public void getChallengeFeedback() throws Exception {
        // Initialize the database
        challengeFeedbackRepository.saveAndFlush(challengeFeedback);

        // Get the challengeFeedback
        restChallengeFeedbackMockMvc.perform(get("/api/challenge-feedbacks/{id}", challengeFeedback.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(challengeFeedback.getId().intValue()))
            .andExpect(jsonPath("$.applicationId").value(DEFAULT_APPLICATION_ID.intValue()))
            .andExpect(jsonPath("$.feedbackForUs").value(DEFAULT_FEEDBACK_FOR_US.toString()))
            .andExpect(jsonPath("$.feedbackForChallenge").value(DEFAULT_FEEDBACK_FOR_CHALLENGE.toString()))
            .andExpect(jsonPath("$.publicFeedback").value(DEFAULT_PUBLIC_FEEDBACK.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING));
    }

    @Test
    @Transactional
    public void getNonExistingChallengeFeedback() throws Exception {
        // Get the challengeFeedback
        restChallengeFeedbackMockMvc.perform(get("/api/challenge-feedbacks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChallengeFeedback() throws Exception {
        // Initialize the database
        challengeFeedbackRepository.saveAndFlush(challengeFeedback);
        int databaseSizeBeforeUpdate = challengeFeedbackRepository.findAll().size();

        // Update the challengeFeedback
        ChallengeFeedback updatedChallengeFeedback = challengeFeedbackRepository.findOne(challengeFeedback.getId());
        updatedChallengeFeedback
                .applicationId(UPDATED_APPLICATION_ID)
                .feedbackForUs(UPDATED_FEEDBACK_FOR_US)
                .feedbackForChallenge(UPDATED_FEEDBACK_FOR_CHALLENGE)
                .publicFeedback(UPDATED_PUBLIC_FEEDBACK)
                .rating(UPDATED_RATING);

        restChallengeFeedbackMockMvc.perform(put("/api/challenge-feedbacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChallengeFeedback)))
                .andExpect(status().isOk());

        // Validate the ChallengeFeedback in the database
        List<ChallengeFeedback> challengeFeedbacks = challengeFeedbackRepository.findAll();
        assertThat(challengeFeedbacks).hasSize(databaseSizeBeforeUpdate);
        ChallengeFeedback testChallengeFeedback = challengeFeedbacks.get(challengeFeedbacks.size() - 1);
        assertThat(testChallengeFeedback.getApplicationId()).isEqualTo(UPDATED_APPLICATION_ID);
        assertThat(testChallengeFeedback.getFeedbackForUs()).isEqualTo(UPDATED_FEEDBACK_FOR_US);
        assertThat(testChallengeFeedback.getFeedbackForChallenge()).isEqualTo(UPDATED_FEEDBACK_FOR_CHALLENGE);
        assertThat(testChallengeFeedback.getPublicFeedback()).isEqualTo(UPDATED_PUBLIC_FEEDBACK);
        assertThat(testChallengeFeedback.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    public void deleteChallengeFeedback() throws Exception {
        // Initialize the database
        challengeFeedbackRepository.saveAndFlush(challengeFeedback);
        int databaseSizeBeforeDelete = challengeFeedbackRepository.findAll().size();

        // Get the challengeFeedback
        restChallengeFeedbackMockMvc.perform(delete("/api/challenge-feedbacks/{id}", challengeFeedback.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChallengeFeedback> challengeFeedbacks = challengeFeedbackRepository.findAll();
        assertThat(challengeFeedbacks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
