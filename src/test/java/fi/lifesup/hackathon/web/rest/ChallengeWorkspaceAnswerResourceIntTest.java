package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.ChallengeWorkspaceAnswer;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceAnswerRepository;

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

import fi.lifesup.hackathon.domain.enumeration.WorkspaceAnswerType;
/**
 * Test class for the ChallengeWorkspaceAnswerResource REST controller.
 *
 * @see ChallengeWorkspaceAnswerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class ChallengeWorkspaceAnswerResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    private static final WorkspaceAnswerType DEFAULT_ANSWER_BY_TYPE = WorkspaceAnswerType.BY_USER;
    private static final WorkspaceAnswerType UPDATED_ANSWER_BY_TYPE = WorkspaceAnswerType.BY_HOST;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CREATED_DATE);

    private static final ZonedDateTime DEFAULT_CREATED_BY = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_BY = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_BY_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CREATED_BY);

    @Inject
    private ChallengeWorkspaceAnswerRepository challengeWorkspaceAnswerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChallengeWorkspaceAnswerMockMvc;

    private ChallengeWorkspaceAnswer challengeWorkspaceAnswer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChallengeWorkspaceAnswerResource challengeWorkspaceAnswerResource = new ChallengeWorkspaceAnswerResource();
        ReflectionTestUtils.setField(challengeWorkspaceAnswerResource, "challengeWorkspaceAnswerRepository", challengeWorkspaceAnswerRepository);
        this.restChallengeWorkspaceAnswerMockMvc = MockMvcBuilders.standaloneSetup(challengeWorkspaceAnswerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChallengeWorkspaceAnswer createEntity(EntityManager em) {
        ChallengeWorkspaceAnswer challengeWorkspaceAnswer = new ChallengeWorkspaceAnswer()
                .content(DEFAULT_CONTENT)
                .answerByType(DEFAULT_ANSWER_BY_TYPE)
                .createdDate(DEFAULT_CREATED_DATE)
                .createdBy(DEFAULT_CREATED_BY);
        return challengeWorkspaceAnswer;
    }

    @Before
    public void initTest() {
        challengeWorkspaceAnswer = createEntity(em);
    }

    @Test
    @Transactional
    public void createChallengeWorkspaceAnswer() throws Exception {
        int databaseSizeBeforeCreate = challengeWorkspaceAnswerRepository.findAll().size();

        // Create the ChallengeWorkspaceAnswer

        restChallengeWorkspaceAnswerMockMvc.perform(post("/api/challenge-workspace-answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeWorkspaceAnswer)))
                .andExpect(status().isCreated());

        // Validate the ChallengeWorkspaceAnswer in the database
        List<ChallengeWorkspaceAnswer> challengeWorkspaceAnswers = challengeWorkspaceAnswerRepository.findAll();
        assertThat(challengeWorkspaceAnswers).hasSize(databaseSizeBeforeCreate + 1);
        ChallengeWorkspaceAnswer testChallengeWorkspaceAnswer = challengeWorkspaceAnswers.get(challengeWorkspaceAnswers.size() - 1);
        assertThat(testChallengeWorkspaceAnswer.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testChallengeWorkspaceAnswer.getAnswerByType()).isEqualTo(DEFAULT_ANSWER_BY_TYPE);
        assertThat(testChallengeWorkspaceAnswer.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testChallengeWorkspaceAnswer.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeWorkspaceAnswerRepository.findAll().size();
        // set the field null
        challengeWorkspaceAnswer.setContent(null);

        // Create the ChallengeWorkspaceAnswer, which fails.

        restChallengeWorkspaceAnswerMockMvc.perform(post("/api/challenge-workspace-answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeWorkspaceAnswer)))
                .andExpect(status().isBadRequest());

        List<ChallengeWorkspaceAnswer> challengeWorkspaceAnswers = challengeWorkspaceAnswerRepository.findAll();
        assertThat(challengeWorkspaceAnswers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnswerByTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeWorkspaceAnswerRepository.findAll().size();
        // set the field null
        challengeWorkspaceAnswer.setAnswerByType(null);

        // Create the ChallengeWorkspaceAnswer, which fails.

        restChallengeWorkspaceAnswerMockMvc.perform(post("/api/challenge-workspace-answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeWorkspaceAnswer)))
                .andExpect(status().isBadRequest());

        List<ChallengeWorkspaceAnswer> challengeWorkspaceAnswers = challengeWorkspaceAnswerRepository.findAll();
        assertThat(challengeWorkspaceAnswers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChallengeWorkspaceAnswers() throws Exception {
        // Initialize the database
        challengeWorkspaceAnswerRepository.saveAndFlush(challengeWorkspaceAnswer);

        // Get all the challengeWorkspaceAnswers
        restChallengeWorkspaceAnswerMockMvc.perform(get("/api/challenge-workspace-answers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(challengeWorkspaceAnswer.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].answerByType").value(hasItem(DEFAULT_ANSWER_BY_TYPE.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY_STR)));
    }

    @Test
    @Transactional
    public void getChallengeWorkspaceAnswer() throws Exception {
        // Initialize the database
        challengeWorkspaceAnswerRepository.saveAndFlush(challengeWorkspaceAnswer);

        // Get the challengeWorkspaceAnswer
        restChallengeWorkspaceAnswerMockMvc.perform(get("/api/challenge-workspace-answers/{id}", challengeWorkspaceAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(challengeWorkspaceAnswer.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.answerByType").value(DEFAULT_ANSWER_BY_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY_STR));
    }

    @Test
    @Transactional
    public void getNonExistingChallengeWorkspaceAnswer() throws Exception {
        // Get the challengeWorkspaceAnswer
        restChallengeWorkspaceAnswerMockMvc.perform(get("/api/challenge-workspace-answers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChallengeWorkspaceAnswer() throws Exception {
        // Initialize the database
        challengeWorkspaceAnswerRepository.saveAndFlush(challengeWorkspaceAnswer);
        int databaseSizeBeforeUpdate = challengeWorkspaceAnswerRepository.findAll().size();

        // Update the challengeWorkspaceAnswer
        ChallengeWorkspaceAnswer updatedChallengeWorkspaceAnswer = challengeWorkspaceAnswerRepository.findOne(challengeWorkspaceAnswer.getId());
        updatedChallengeWorkspaceAnswer
                .content(UPDATED_CONTENT)
                .answerByType(UPDATED_ANSWER_BY_TYPE)
                .createdDate(UPDATED_CREATED_DATE)
                .createdBy(UPDATED_CREATED_BY);

        restChallengeWorkspaceAnswerMockMvc.perform(put("/api/challenge-workspace-answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChallengeWorkspaceAnswer)))
                .andExpect(status().isOk());

        // Validate the ChallengeWorkspaceAnswer in the database
        List<ChallengeWorkspaceAnswer> challengeWorkspaceAnswers = challengeWorkspaceAnswerRepository.findAll();
        assertThat(challengeWorkspaceAnswers).hasSize(databaseSizeBeforeUpdate);
        ChallengeWorkspaceAnswer testChallengeWorkspaceAnswer = challengeWorkspaceAnswers.get(challengeWorkspaceAnswers.size() - 1);
        assertThat(testChallengeWorkspaceAnswer.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testChallengeWorkspaceAnswer.getAnswerByType()).isEqualTo(UPDATED_ANSWER_BY_TYPE);
        assertThat(testChallengeWorkspaceAnswer.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testChallengeWorkspaceAnswer.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void deleteChallengeWorkspaceAnswer() throws Exception {
        // Initialize the database
        challengeWorkspaceAnswerRepository.saveAndFlush(challengeWorkspaceAnswer);
        int databaseSizeBeforeDelete = challengeWorkspaceAnswerRepository.findAll().size();

        // Get the challengeWorkspaceAnswer
        restChallengeWorkspaceAnswerMockMvc.perform(delete("/api/challenge-workspace-answers/{id}", challengeWorkspaceAnswer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChallengeWorkspaceAnswer> challengeWorkspaceAnswers = challengeWorkspaceAnswerRepository.findAll();
        assertThat(challengeWorkspaceAnswers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
