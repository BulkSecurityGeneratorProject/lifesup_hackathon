package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.ChallengeWorkspaceNews;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceNewsRepository;

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
 * Test class for the ChallengeWorkspaceNewsResource REST controller.
 *
 * @see ChallengeWorkspaceNewsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class ChallengeWorkspaceNewsResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CREATED_DATE);

    private static final String DEFAULT_CREATED_BY = "AAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBB";

    @Inject
    private ChallengeWorkspaceNewsRepository challengeWorkspaceNewsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChallengeWorkspaceNewsMockMvc;

    private ChallengeWorkspaceNews challengeWorkspaceNews;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChallengeWorkspaceNewsResource challengeWorkspaceNewsResource = new ChallengeWorkspaceNewsResource();
        ReflectionTestUtils.setField(challengeWorkspaceNewsResource, "challengeWorkspaceNewsRepository", challengeWorkspaceNewsRepository);
        this.restChallengeWorkspaceNewsMockMvc = MockMvcBuilders.standaloneSetup(challengeWorkspaceNewsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChallengeWorkspaceNews createEntity(EntityManager em) {
        ChallengeWorkspaceNews challengeWorkspaceNews = new ChallengeWorkspaceNews()
                .title(DEFAULT_TITLE)
                .content(DEFAULT_CONTENT)
                .createdDate(DEFAULT_CREATED_DATE)
                .createdBy(DEFAULT_CREATED_BY);
        return challengeWorkspaceNews;
    }

    @Before
    public void initTest() {
        challengeWorkspaceNews = createEntity(em);
    }

    @Test
    @Transactional
    public void createChallengeWorkspaceNews() throws Exception {
        int databaseSizeBeforeCreate = challengeWorkspaceNewsRepository.findAll().size();

        // Create the ChallengeWorkspaceNews

        restChallengeWorkspaceNewsMockMvc.perform(post("/api/challenge-workspace-news")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeWorkspaceNews)))
                .andExpect(status().isCreated());

        // Validate the ChallengeWorkspaceNews in the database
        List<ChallengeWorkspaceNews> challengeWorkspaceNews = challengeWorkspaceNewsRepository.findAll();
        assertThat(challengeWorkspaceNews).hasSize(databaseSizeBeforeCreate + 1);
        ChallengeWorkspaceNews testChallengeWorkspaceNews = challengeWorkspaceNews.get(challengeWorkspaceNews.size() - 1);
        assertThat(testChallengeWorkspaceNews.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testChallengeWorkspaceNews.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testChallengeWorkspaceNews.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testChallengeWorkspaceNews.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeWorkspaceNewsRepository.findAll().size();
        // set the field null
        challengeWorkspaceNews.setTitle(null);

        // Create the ChallengeWorkspaceNews, which fails.

        restChallengeWorkspaceNewsMockMvc.perform(post("/api/challenge-workspace-news")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeWorkspaceNews)))
                .andExpect(status().isBadRequest());

        List<ChallengeWorkspaceNews> challengeWorkspaceNews = challengeWorkspaceNewsRepository.findAll();
        assertThat(challengeWorkspaceNews).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeWorkspaceNewsRepository.findAll().size();
        // set the field null
        challengeWorkspaceNews.setContent(null);

        // Create the ChallengeWorkspaceNews, which fails.

        restChallengeWorkspaceNewsMockMvc.perform(post("/api/challenge-workspace-news")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeWorkspaceNews)))
                .andExpect(status().isBadRequest());

        List<ChallengeWorkspaceNews> challengeWorkspaceNews = challengeWorkspaceNewsRepository.findAll();
        assertThat(challengeWorkspaceNews).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChallengeWorkspaceNews() throws Exception {
        // Initialize the database
        challengeWorkspaceNewsRepository.saveAndFlush(challengeWorkspaceNews);

        // Get all the challengeWorkspaceNews
        restChallengeWorkspaceNewsMockMvc.perform(get("/api/challenge-workspace-news?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(challengeWorkspaceNews.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getChallengeWorkspaceNews() throws Exception {
        // Initialize the database
        challengeWorkspaceNewsRepository.saveAndFlush(challengeWorkspaceNews);

        // Get the challengeWorkspaceNews
        restChallengeWorkspaceNewsMockMvc.perform(get("/api/challenge-workspace-news/{id}", challengeWorkspaceNews.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(challengeWorkspaceNews.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChallengeWorkspaceNews() throws Exception {
        // Get the challengeWorkspaceNews
        restChallengeWorkspaceNewsMockMvc.perform(get("/api/challenge-workspace-news/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChallengeWorkspaceNews() throws Exception {
        // Initialize the database
        challengeWorkspaceNewsRepository.saveAndFlush(challengeWorkspaceNews);
        int databaseSizeBeforeUpdate = challengeWorkspaceNewsRepository.findAll().size();

        // Update the challengeWorkspaceNews
        ChallengeWorkspaceNews updatedChallengeWorkspaceNews = challengeWorkspaceNewsRepository.findOne(challengeWorkspaceNews.getId());
        updatedChallengeWorkspaceNews
                .title(UPDATED_TITLE)
                .content(UPDATED_CONTENT)
                .createdDate(UPDATED_CREATED_DATE)
                .createdBy(UPDATED_CREATED_BY);

        restChallengeWorkspaceNewsMockMvc.perform(put("/api/challenge-workspace-news")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChallengeWorkspaceNews)))
                .andExpect(status().isOk());

        // Validate the ChallengeWorkspaceNews in the database
        List<ChallengeWorkspaceNews> challengeWorkspaceNews = challengeWorkspaceNewsRepository.findAll();
        assertThat(challengeWorkspaceNews).hasSize(databaseSizeBeforeUpdate);
        ChallengeWorkspaceNews testChallengeWorkspaceNews = challengeWorkspaceNews.get(challengeWorkspaceNews.size() - 1);
        assertThat(testChallengeWorkspaceNews.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testChallengeWorkspaceNews.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testChallengeWorkspaceNews.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testChallengeWorkspaceNews.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void deleteChallengeWorkspaceNews() throws Exception {
        // Initialize the database
        challengeWorkspaceNewsRepository.saveAndFlush(challengeWorkspaceNews);
        int databaseSizeBeforeDelete = challengeWorkspaceNewsRepository.findAll().size();

        // Get the challengeWorkspaceNews
        restChallengeWorkspaceNewsMockMvc.perform(delete("/api/challenge-workspace-news/{id}", challengeWorkspaceNews.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChallengeWorkspaceNews> challengeWorkspaceNews = challengeWorkspaceNewsRepository.findAll();
        assertThat(challengeWorkspaceNews).hasSize(databaseSizeBeforeDelete - 1);
    }
}
