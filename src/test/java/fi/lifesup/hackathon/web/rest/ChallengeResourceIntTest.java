package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.repository.ChallengeRepository;

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
 * Test class for the ChallengeResource REST controller.
 *
 * @see ChallengeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class ChallengeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_INTRODUCTION = "AAAAA";
    private static final String UPDATED_INTRODUCTION = "BBBBB";

    private static final String DEFAULT_CHALLENGE_TEXT = "AAAAA";
    private static final String UPDATED_CHALLENGE_TEXT = "BBBBB";

    private static final String DEFAULT_RESOURCE_TEXT = "AAAAA";
    private static final String UPDATED_RESOURCE_TEXT = "BBBBB";

    private static final String DEFAULT_REWARDS_TEXT = "AAAAA";
    private static final String UPDATED_REWARDS_TEXT = "BBBBB";

    private static final String DEFAULT_TIMELINE_TEXT = "AAAAA";
    private static final String UPDATED_TIMELINE_TEXT = "BBBBB";

    private static final String DEFAULT_RULES_TEXT = "AAAAA";
    private static final String UPDATED_RULES_TEXT = "BBBBB";

    private static final String DEFAULT_BANNER_URL = "AAAAA";
    private static final String UPDATED_BANNER_URL = "BBBBB";

    private static final String DEFAULT_ADDITIONAL_TEXT = "AAAAA";
    private static final String UPDATED_ADDITIONAL_TEXT = "BBBBB";

    private static final Integer DEFAULT_MAX_TEAM_NUMBER = 1;
    private static final Integer UPDATED_MAX_TEAM_NUMBER = 2;

    private static final Integer DEFAULT_MIN_TEAM_NUMBER = 1;
    private static final Integer UPDATED_MIN_TEAM_NUMBER = 2;

    @Inject
    private ChallengeRepository challengeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChallengeMockMvc;

    private Challenge challenge;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChallengeResource challengeResource = new ChallengeResource();
        ReflectionTestUtils.setField(challengeResource, "challengeRepository", challengeRepository);
        this.restChallengeMockMvc = MockMvcBuilders.standaloneSetup(challengeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Challenge createEntity(EntityManager em) {
        Challenge challenge = new Challenge()
                .name(DEFAULT_NAME)
                .introduction(DEFAULT_INTRODUCTION)
                .challengeText(DEFAULT_CHALLENGE_TEXT)
                .resourceText(DEFAULT_RESOURCE_TEXT)
                .rewardsText(DEFAULT_REWARDS_TEXT)
                .timelineText(DEFAULT_TIMELINE_TEXT)
                .rulesText(DEFAULT_RULES_TEXT)
                .bannerUrl(DEFAULT_BANNER_URL)
                .additionalText(DEFAULT_ADDITIONAL_TEXT)
                .maxTeamNumber(DEFAULT_MAX_TEAM_NUMBER)
                .minTeamNumber(DEFAULT_MIN_TEAM_NUMBER);
        return challenge;
    }

    @Before
    public void initTest() {
        challenge = createEntity(em);
    }

    @Test
    @Transactional
    public void createChallenge() throws Exception {
        int databaseSizeBeforeCreate = challengeRepository.findAll().size();

        // Create the Challenge

        restChallengeMockMvc.perform(post("/api/challenges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challenge)))
                .andExpect(status().isCreated());

        // Validate the Challenge in the database
        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeCreate + 1);
        Challenge testChallenge = challenges.get(challenges.size() - 1);
        assertThat(testChallenge.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChallenge.getIntroduction()).isEqualTo(DEFAULT_INTRODUCTION);
        assertThat(testChallenge.getChallengeText()).isEqualTo(DEFAULT_CHALLENGE_TEXT);
        assertThat(testChallenge.getResourceText()).isEqualTo(DEFAULT_RESOURCE_TEXT);
        assertThat(testChallenge.getRewardsText()).isEqualTo(DEFAULT_REWARDS_TEXT);
        assertThat(testChallenge.getTimelineText()).isEqualTo(DEFAULT_TIMELINE_TEXT);
        assertThat(testChallenge.getRulesText()).isEqualTo(DEFAULT_RULES_TEXT);
        assertThat(testChallenge.getBannerUrl()).isEqualTo(DEFAULT_BANNER_URL);
        assertThat(testChallenge.getAdditionalText()).isEqualTo(DEFAULT_ADDITIONAL_TEXT);
        assertThat(testChallenge.getMaxTeamNumber()).isEqualTo(DEFAULT_MAX_TEAM_NUMBER);
        assertThat(testChallenge.getMinTeamNumber()).isEqualTo(DEFAULT_MIN_TEAM_NUMBER);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setName(null);

        // Create the Challenge, which fails.

        restChallengeMockMvc.perform(post("/api/challenges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challenge)))
                .andExpect(status().isBadRequest());

        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIntroductionIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setIntroduction(null);

        // Create the Challenge, which fails.

        restChallengeMockMvc.perform(post("/api/challenges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challenge)))
                .andExpect(status().isBadRequest());

        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChallengeTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setChallengeText(null);

        // Create the Challenge, which fails.

        restChallengeMockMvc.perform(post("/api/challenges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challenge)))
                .andExpect(status().isBadRequest());

        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResourceTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setResourceText(null);

        // Create the Challenge, which fails.

        restChallengeMockMvc.perform(post("/api/challenges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challenge)))
                .andExpect(status().isBadRequest());

        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRewardsTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setRewardsText(null);

        // Create the Challenge, which fails.

        restChallengeMockMvc.perform(post("/api/challenges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challenge)))
                .andExpect(status().isBadRequest());

        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimelineTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setTimelineText(null);

        // Create the Challenge, which fails.

        restChallengeMockMvc.perform(post("/api/challenges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challenge)))
                .andExpect(status().isBadRequest());

        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRulesTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setRulesText(null);

        // Create the Challenge, which fails.

        restChallengeMockMvc.perform(post("/api/challenges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challenge)))
                .andExpect(status().isBadRequest());

        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBannerUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setBannerUrl(null);

        // Create the Challenge, which fails.

        restChallengeMockMvc.perform(post("/api/challenges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challenge)))
                .andExpect(status().isBadRequest());

        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxTeamNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setMaxTeamNumber(null);

        // Create the Challenge, which fails.

        restChallengeMockMvc.perform(post("/api/challenges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challenge)))
                .andExpect(status().isBadRequest());

        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMinTeamNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setMinTeamNumber(null);

        // Create the Challenge, which fails.

        restChallengeMockMvc.perform(post("/api/challenges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challenge)))
                .andExpect(status().isBadRequest());

        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChallenges() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challenges
        restChallengeMockMvc.perform(get("/api/challenges?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(challenge.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].introduction").value(hasItem(DEFAULT_INTRODUCTION.toString())))
                .andExpect(jsonPath("$.[*].challengeText").value(hasItem(DEFAULT_CHALLENGE_TEXT.toString())))
                .andExpect(jsonPath("$.[*].resourceText").value(hasItem(DEFAULT_RESOURCE_TEXT.toString())))
                .andExpect(jsonPath("$.[*].rewardsText").value(hasItem(DEFAULT_REWARDS_TEXT.toString())))
                .andExpect(jsonPath("$.[*].timelineText").value(hasItem(DEFAULT_TIMELINE_TEXT.toString())))
                .andExpect(jsonPath("$.[*].rulesText").value(hasItem(DEFAULT_RULES_TEXT.toString())))
                .andExpect(jsonPath("$.[*].bannerUrl").value(hasItem(DEFAULT_BANNER_URL.toString())))
                .andExpect(jsonPath("$.[*].additionalText").value(hasItem(DEFAULT_ADDITIONAL_TEXT.toString())))
                .andExpect(jsonPath("$.[*].maxTeamNumber").value(hasItem(DEFAULT_MAX_TEAM_NUMBER)))
                .andExpect(jsonPath("$.[*].minTeamNumber").value(hasItem(DEFAULT_MIN_TEAM_NUMBER)));
    }

    @Test
    @Transactional
    public void getChallenge() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get the challenge
        restChallengeMockMvc.perform(get("/api/challenges/{id}", challenge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(challenge.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.introduction").value(DEFAULT_INTRODUCTION.toString()))
            .andExpect(jsonPath("$.challengeText").value(DEFAULT_CHALLENGE_TEXT.toString()))
            .andExpect(jsonPath("$.resourceText").value(DEFAULT_RESOURCE_TEXT.toString()))
            .andExpect(jsonPath("$.rewardsText").value(DEFAULT_REWARDS_TEXT.toString()))
            .andExpect(jsonPath("$.timelineText").value(DEFAULT_TIMELINE_TEXT.toString()))
            .andExpect(jsonPath("$.rulesText").value(DEFAULT_RULES_TEXT.toString()))
            .andExpect(jsonPath("$.bannerUrl").value(DEFAULT_BANNER_URL.toString()))
            .andExpect(jsonPath("$.additionalText").value(DEFAULT_ADDITIONAL_TEXT.toString()))
            .andExpect(jsonPath("$.maxTeamNumber").value(DEFAULT_MAX_TEAM_NUMBER))
            .andExpect(jsonPath("$.minTeamNumber").value(DEFAULT_MIN_TEAM_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingChallenge() throws Exception {
        // Get the challenge
        restChallengeMockMvc.perform(get("/api/challenges/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChallenge() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);
        int databaseSizeBeforeUpdate = challengeRepository.findAll().size();

        // Update the challenge
        Challenge updatedChallenge = challengeRepository.findOne(challenge.getId());
        updatedChallenge
                .name(UPDATED_NAME)
                .introduction(UPDATED_INTRODUCTION)
                .challengeText(UPDATED_CHALLENGE_TEXT)
                .resourceText(UPDATED_RESOURCE_TEXT)
                .rewardsText(UPDATED_REWARDS_TEXT)
                .timelineText(UPDATED_TIMELINE_TEXT)
                .rulesText(UPDATED_RULES_TEXT)
                .bannerUrl(UPDATED_BANNER_URL)
                .additionalText(UPDATED_ADDITIONAL_TEXT)
                .maxTeamNumber(UPDATED_MAX_TEAM_NUMBER)
                .minTeamNumber(UPDATED_MIN_TEAM_NUMBER);

        restChallengeMockMvc.perform(put("/api/challenges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChallenge)))
                .andExpect(status().isOk());

        // Validate the Challenge in the database
        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeUpdate);
        Challenge testChallenge = challenges.get(challenges.size() - 1);
        assertThat(testChallenge.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChallenge.getIntroduction()).isEqualTo(UPDATED_INTRODUCTION);
        assertThat(testChallenge.getChallengeText()).isEqualTo(UPDATED_CHALLENGE_TEXT);
        assertThat(testChallenge.getResourceText()).isEqualTo(UPDATED_RESOURCE_TEXT);
        assertThat(testChallenge.getRewardsText()).isEqualTo(UPDATED_REWARDS_TEXT);
        assertThat(testChallenge.getTimelineText()).isEqualTo(UPDATED_TIMELINE_TEXT);
        assertThat(testChallenge.getRulesText()).isEqualTo(UPDATED_RULES_TEXT);
        assertThat(testChallenge.getBannerUrl()).isEqualTo(UPDATED_BANNER_URL);
        assertThat(testChallenge.getAdditionalText()).isEqualTo(UPDATED_ADDITIONAL_TEXT);
        assertThat(testChallenge.getMaxTeamNumber()).isEqualTo(UPDATED_MAX_TEAM_NUMBER);
        assertThat(testChallenge.getMinTeamNumber()).isEqualTo(UPDATED_MIN_TEAM_NUMBER);
    }

    @Test
    @Transactional
    public void deleteChallenge() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);
        int databaseSizeBeforeDelete = challengeRepository.findAll().size();

        // Get the challenge
        restChallengeMockMvc.perform(delete("/api/challenges/{id}", challenge.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Challenge> challenges = challengeRepository.findAll();
        assertThat(challenges).hasSize(databaseSizeBeforeDelete - 1);
    }
}
