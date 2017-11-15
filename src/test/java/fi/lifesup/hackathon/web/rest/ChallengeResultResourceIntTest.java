package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.ChallengeResult;
import fi.lifesup.hackathon.repository.ChallengeResultRepository;

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
 * Test class for the ChallengeResultResource REST controller.
 *
 * @see ChallengeResultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class ChallengeResultResourceIntTest {

    private static final Long DEFAULT_FIRST_TEAM = 1L;
    private static final Long UPDATED_FIRST_TEAM = 2L;

    private static final Long DEFAULT_SECOND_TEAM = 1L;
    private static final Long UPDATED_SECOND_TEAM = 2L;

    private static final Long DEFAULT_THIRD_TEAM = 1L;
    private static final Long UPDATED_THIRD_TEAM = 2L;

    private static final String DEFAULT_ADDITIONAL_NOTE = "AAAAA";
    private static final String UPDATED_ADDITIONAL_NOTE = "BBBBB";

    @Inject
    private ChallengeResultRepository challengeResultRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChallengeResultMockMvc;

    private ChallengeResult challengeResult;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChallengeResultResource challengeResultResource = new ChallengeResultResource();
        ReflectionTestUtils.setField(challengeResultResource, "challengeResultRepository", challengeResultRepository);
        this.restChallengeResultMockMvc = MockMvcBuilders.standaloneSetup(challengeResultResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChallengeResult createEntity(EntityManager em) {
        ChallengeResult challengeResult = new ChallengeResult()
                .firstTeam(DEFAULT_FIRST_TEAM)
                .secondTeam(DEFAULT_SECOND_TEAM)
                .thirdTeam(DEFAULT_THIRD_TEAM)
                .additionalNote(DEFAULT_ADDITIONAL_NOTE);
        return challengeResult;
    }

    @Before
    public void initTest() {
        challengeResult = createEntity(em);
    }

    @Test
    @Transactional
    public void createChallengeResult() throws Exception {
        int databaseSizeBeforeCreate = challengeResultRepository.findAll().size();

        // Create the ChallengeResult

        restChallengeResultMockMvc.perform(post("/api/challenge-results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeResult)))
                .andExpect(status().isCreated());

        // Validate the ChallengeResult in the database
        List<ChallengeResult> challengeResults = challengeResultRepository.findAll();
        assertThat(challengeResults).hasSize(databaseSizeBeforeCreate + 1);
        ChallengeResult testChallengeResult = challengeResults.get(challengeResults.size() - 1);
        assertThat(testChallengeResult.getFirstTeam()).isEqualTo(DEFAULT_FIRST_TEAM);
        assertThat(testChallengeResult.getSecondTeam()).isEqualTo(DEFAULT_SECOND_TEAM);
        assertThat(testChallengeResult.getThirdTeam()).isEqualTo(DEFAULT_THIRD_TEAM);
        assertThat(testChallengeResult.getAdditionalNote()).isEqualTo(DEFAULT_ADDITIONAL_NOTE);
    }

    @Test
    @Transactional
    public void checkFirstTeamIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeResultRepository.findAll().size();
        // set the field null
        challengeResult.setFirstTeam(null);

        // Create the ChallengeResult, which fails.

        restChallengeResultMockMvc.perform(post("/api/challenge-results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeResult)))
                .andExpect(status().isBadRequest());

        List<ChallengeResult> challengeResults = challengeResultRepository.findAll();
        assertThat(challengeResults).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChallengeResults() throws Exception {
        // Initialize the database
        challengeResultRepository.saveAndFlush(challengeResult);

        // Get all the challengeResults
        restChallengeResultMockMvc.perform(get("/api/challenge-results?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(challengeResult.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstTeam").value(hasItem(DEFAULT_FIRST_TEAM.intValue())))
                .andExpect(jsonPath("$.[*].secondTeam").value(hasItem(DEFAULT_SECOND_TEAM.intValue())))
                .andExpect(jsonPath("$.[*].thirdTeam").value(hasItem(DEFAULT_THIRD_TEAM.intValue())))
                .andExpect(jsonPath("$.[*].additionalNote").value(hasItem(DEFAULT_ADDITIONAL_NOTE.toString())));
    }

    @Test
    @Transactional
    public void getChallengeResult() throws Exception {
        // Initialize the database
        challengeResultRepository.saveAndFlush(challengeResult);

        // Get the challengeResult
        restChallengeResultMockMvc.perform(get("/api/challenge-results/{id}", challengeResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(challengeResult.getId().intValue()))
            .andExpect(jsonPath("$.firstTeam").value(DEFAULT_FIRST_TEAM.intValue()))
            .andExpect(jsonPath("$.secondTeam").value(DEFAULT_SECOND_TEAM.intValue()))
            .andExpect(jsonPath("$.thirdTeam").value(DEFAULT_THIRD_TEAM.intValue()))
            .andExpect(jsonPath("$.additionalNote").value(DEFAULT_ADDITIONAL_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChallengeResult() throws Exception {
        // Get the challengeResult
        restChallengeResultMockMvc.perform(get("/api/challenge-results/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChallengeResult() throws Exception {
        // Initialize the database
        challengeResultRepository.saveAndFlush(challengeResult);
        int databaseSizeBeforeUpdate = challengeResultRepository.findAll().size();

        // Update the challengeResult
        ChallengeResult updatedChallengeResult = challengeResultRepository.findOne(challengeResult.getId());
        updatedChallengeResult
                .firstTeam(UPDATED_FIRST_TEAM)
                .secondTeam(UPDATED_SECOND_TEAM)
                .thirdTeam(UPDATED_THIRD_TEAM)
                .additionalNote(UPDATED_ADDITIONAL_NOTE);

        restChallengeResultMockMvc.perform(put("/api/challenge-results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChallengeResult)))
                .andExpect(status().isOk());

        // Validate the ChallengeResult in the database
        List<ChallengeResult> challengeResults = challengeResultRepository.findAll();
        assertThat(challengeResults).hasSize(databaseSizeBeforeUpdate);
        ChallengeResult testChallengeResult = challengeResults.get(challengeResults.size() - 1);
        assertThat(testChallengeResult.getFirstTeam()).isEqualTo(UPDATED_FIRST_TEAM);
        assertThat(testChallengeResult.getSecondTeam()).isEqualTo(UPDATED_SECOND_TEAM);
        assertThat(testChallengeResult.getThirdTeam()).isEqualTo(UPDATED_THIRD_TEAM);
        assertThat(testChallengeResult.getAdditionalNote()).isEqualTo(UPDATED_ADDITIONAL_NOTE);
    }

    @Test
    @Transactional
    public void deleteChallengeResult() throws Exception {
        // Initialize the database
        challengeResultRepository.saveAndFlush(challengeResult);
        int databaseSizeBeforeDelete = challengeResultRepository.findAll().size();

        // Get the challengeResult
        restChallengeResultMockMvc.perform(delete("/api/challenge-results/{id}", challengeResult.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChallengeResult> challengeResults = challengeResultRepository.findAll();
        assertThat(challengeResults).hasSize(databaseSizeBeforeDelete - 1);
    }
}
