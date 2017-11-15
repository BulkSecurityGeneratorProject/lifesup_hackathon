package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.ChallengeWorkspaceQuestion;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceQuestionRepository;

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
 * Test class for the ChallengeWorkspaceQuestionResource REST controller.
 *
 * @see ChallengeWorkspaceQuestionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class ChallengeWorkspaceQuestionResourceIntTest {

    private static final Long DEFAULT_APPLICATION_ID = 1L;
    private static final Long UPDATED_APPLICATION_ID = 2L;

    private static final String DEFAULT_SUBJECT = "AAAAA";
    private static final String UPDATED_SUBJECT = "BBBBB";

    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CREATED_DATE);

    private static final String DEFAULT_CREATED_BY = "AAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBB";

    @Inject
    private ChallengeWorkspaceQuestionRepository challengeWorkspaceQuestionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChallengeWorkspaceQuestionMockMvc;

    private ChallengeWorkspaceQuestion challengeWorkspaceQuestion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChallengeWorkspaceQuestionResource challengeWorkspaceQuestionResource = new ChallengeWorkspaceQuestionResource();
        ReflectionTestUtils.setField(challengeWorkspaceQuestionResource, "challengeWorkspaceQuestionRepository", challengeWorkspaceQuestionRepository);
        this.restChallengeWorkspaceQuestionMockMvc = MockMvcBuilders.standaloneSetup(challengeWorkspaceQuestionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChallengeWorkspaceQuestion createEntity(EntityManager em) {
        ChallengeWorkspaceQuestion challengeWorkspaceQuestion = new ChallengeWorkspaceQuestion()
                .applicationId(DEFAULT_APPLICATION_ID)
                .subject(DEFAULT_SUBJECT)
                .content(DEFAULT_CONTENT)
                .createdDate(DEFAULT_CREATED_DATE)
                .createdBy(DEFAULT_CREATED_BY);
        return challengeWorkspaceQuestion;
    }

    @Before
    public void initTest() {
        challengeWorkspaceQuestion = createEntity(em);
    }

    @Test
    @Transactional
    public void createChallengeWorkspaceQuestion() throws Exception {
        int databaseSizeBeforeCreate = challengeWorkspaceQuestionRepository.findAll().size();

        // Create the ChallengeWorkspaceQuestion

        restChallengeWorkspaceQuestionMockMvc.perform(post("/api/challenge-workspace-questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeWorkspaceQuestion)))
                .andExpect(status().isCreated());

        // Validate the ChallengeWorkspaceQuestion in the database
        List<ChallengeWorkspaceQuestion> challengeWorkspaceQuestions = challengeWorkspaceQuestionRepository.findAll();
        assertThat(challengeWorkspaceQuestions).hasSize(databaseSizeBeforeCreate + 1);
        ChallengeWorkspaceQuestion testChallengeWorkspaceQuestion = challengeWorkspaceQuestions.get(challengeWorkspaceQuestions.size() - 1);
        assertThat(testChallengeWorkspaceQuestion.getApplicationId()).isEqualTo(DEFAULT_APPLICATION_ID);
        assertThat(testChallengeWorkspaceQuestion.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testChallengeWorkspaceQuestion.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testChallengeWorkspaceQuestion.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testChallengeWorkspaceQuestion.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    public void checkApplicationIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeWorkspaceQuestionRepository.findAll().size();
        // set the field null
        challengeWorkspaceQuestion.setApplicationId(null);

        // Create the ChallengeWorkspaceQuestion, which fails.

        restChallengeWorkspaceQuestionMockMvc.perform(post("/api/challenge-workspace-questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeWorkspaceQuestion)))
                .andExpect(status().isBadRequest());

        List<ChallengeWorkspaceQuestion> challengeWorkspaceQuestions = challengeWorkspaceQuestionRepository.findAll();
        assertThat(challengeWorkspaceQuestions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeWorkspaceQuestionRepository.findAll().size();
        // set the field null
        challengeWorkspaceQuestion.setSubject(null);

        // Create the ChallengeWorkspaceQuestion, which fails.

        restChallengeWorkspaceQuestionMockMvc.perform(post("/api/challenge-workspace-questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeWorkspaceQuestion)))
                .andExpect(status().isBadRequest());

        List<ChallengeWorkspaceQuestion> challengeWorkspaceQuestions = challengeWorkspaceQuestionRepository.findAll();
        assertThat(challengeWorkspaceQuestions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeWorkspaceQuestionRepository.findAll().size();
        // set the field null
        challengeWorkspaceQuestion.setContent(null);

        // Create the ChallengeWorkspaceQuestion, which fails.

        restChallengeWorkspaceQuestionMockMvc.perform(post("/api/challenge-workspace-questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeWorkspaceQuestion)))
                .andExpect(status().isBadRequest());

        List<ChallengeWorkspaceQuestion> challengeWorkspaceQuestions = challengeWorkspaceQuestionRepository.findAll();
        assertThat(challengeWorkspaceQuestions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChallengeWorkspaceQuestions() throws Exception {
        // Initialize the database
        challengeWorkspaceQuestionRepository.saveAndFlush(challengeWorkspaceQuestion);

        // Get all the challengeWorkspaceQuestions
        restChallengeWorkspaceQuestionMockMvc.perform(get("/api/challenge-workspace-questions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(challengeWorkspaceQuestion.getId().intValue())))
                .andExpect(jsonPath("$.[*].applicationId").value(hasItem(DEFAULT_APPLICATION_ID.intValue())))
                .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getChallengeWorkspaceQuestion() throws Exception {
        // Initialize the database
        challengeWorkspaceQuestionRepository.saveAndFlush(challengeWorkspaceQuestion);

        // Get the challengeWorkspaceQuestion
        restChallengeWorkspaceQuestionMockMvc.perform(get("/api/challenge-workspace-questions/{id}", challengeWorkspaceQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(challengeWorkspaceQuestion.getId().intValue()))
            .andExpect(jsonPath("$.applicationId").value(DEFAULT_APPLICATION_ID.intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChallengeWorkspaceQuestion() throws Exception {
        // Get the challengeWorkspaceQuestion
        restChallengeWorkspaceQuestionMockMvc.perform(get("/api/challenge-workspace-questions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChallengeWorkspaceQuestion() throws Exception {
        // Initialize the database
        challengeWorkspaceQuestionRepository.saveAndFlush(challengeWorkspaceQuestion);
        int databaseSizeBeforeUpdate = challengeWorkspaceQuestionRepository.findAll().size();

        // Update the challengeWorkspaceQuestion
        ChallengeWorkspaceQuestion updatedChallengeWorkspaceQuestion = challengeWorkspaceQuestionRepository.findOne(challengeWorkspaceQuestion.getId());
        updatedChallengeWorkspaceQuestion
                .applicationId(UPDATED_APPLICATION_ID)
                .subject(UPDATED_SUBJECT)
                .content(UPDATED_CONTENT)
                .createdDate(UPDATED_CREATED_DATE)
                .createdBy(UPDATED_CREATED_BY);

        restChallengeWorkspaceQuestionMockMvc.perform(put("/api/challenge-workspace-questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChallengeWorkspaceQuestion)))
                .andExpect(status().isOk());

        // Validate the ChallengeWorkspaceQuestion in the database
        List<ChallengeWorkspaceQuestion> challengeWorkspaceQuestions = challengeWorkspaceQuestionRepository.findAll();
        assertThat(challengeWorkspaceQuestions).hasSize(databaseSizeBeforeUpdate);
        ChallengeWorkspaceQuestion testChallengeWorkspaceQuestion = challengeWorkspaceQuestions.get(challengeWorkspaceQuestions.size() - 1);
        assertThat(testChallengeWorkspaceQuestion.getApplicationId()).isEqualTo(UPDATED_APPLICATION_ID);
        assertThat(testChallengeWorkspaceQuestion.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testChallengeWorkspaceQuestion.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testChallengeWorkspaceQuestion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testChallengeWorkspaceQuestion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void deleteChallengeWorkspaceQuestion() throws Exception {
        // Initialize the database
        challengeWorkspaceQuestionRepository.saveAndFlush(challengeWorkspaceQuestion);
        int databaseSizeBeforeDelete = challengeWorkspaceQuestionRepository.findAll().size();

        // Get the challengeWorkspaceQuestion
        restChallengeWorkspaceQuestionMockMvc.perform(delete("/api/challenge-workspace-questions/{id}", challengeWorkspaceQuestion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChallengeWorkspaceQuestion> challengeWorkspaceQuestions = challengeWorkspaceQuestionRepository.findAll();
        assertThat(challengeWorkspaceQuestions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
