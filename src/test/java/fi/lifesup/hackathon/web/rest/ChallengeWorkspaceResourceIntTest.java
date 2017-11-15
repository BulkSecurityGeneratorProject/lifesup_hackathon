package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.ChallengeWorkspace;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceRepository;

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
 * Test class for the ChallengeWorkspaceResource REST controller.
 *
 * @see ChallengeWorkspaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class ChallengeWorkspaceResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CREATED_DATE);

    private static final String DEFAULT_TERMS_AND_CONDITIONS = "AAAAA";
    private static final String UPDATED_TERMS_AND_CONDITIONS = "BBBBB";

    @Inject
    private ChallengeWorkspaceRepository challengeWorkspaceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChallengeWorkspaceMockMvc;

    private ChallengeWorkspace challengeWorkspace;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChallengeWorkspaceResource challengeWorkspaceResource = new ChallengeWorkspaceResource();
        ReflectionTestUtils.setField(challengeWorkspaceResource, "challengeWorkspaceRepository", challengeWorkspaceRepository);
        this.restChallengeWorkspaceMockMvc = MockMvcBuilders.standaloneSetup(challengeWorkspaceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChallengeWorkspace createEntity(EntityManager em) {
        ChallengeWorkspace challengeWorkspace = new ChallengeWorkspace()
                .createdDate(DEFAULT_CREATED_DATE)
                .termsAndConditions(DEFAULT_TERMS_AND_CONDITIONS);
        return challengeWorkspace;
    }

    @Before
    public void initTest() {
        challengeWorkspace = createEntity(em);
    }

    @Test
    @Transactional
    public void createChallengeWorkspace() throws Exception {
        int databaseSizeBeforeCreate = challengeWorkspaceRepository.findAll().size();

        // Create the ChallengeWorkspace

        restChallengeWorkspaceMockMvc.perform(post("/api/challenge-workspaces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(challengeWorkspace)))
                .andExpect(status().isCreated());

        // Validate the ChallengeWorkspace in the database
        List<ChallengeWorkspace> challengeWorkspaces = challengeWorkspaceRepository.findAll();
        assertThat(challengeWorkspaces).hasSize(databaseSizeBeforeCreate + 1);
        ChallengeWorkspace testChallengeWorkspace = challengeWorkspaces.get(challengeWorkspaces.size() - 1);
        assertThat(testChallengeWorkspace.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testChallengeWorkspace.getTermsAndConditions()).isEqualTo(DEFAULT_TERMS_AND_CONDITIONS);
    }

    @Test
    @Transactional
    public void getAllChallengeWorkspaces() throws Exception {
        // Initialize the database
        challengeWorkspaceRepository.saveAndFlush(challengeWorkspace);

        // Get all the challengeWorkspaces
        restChallengeWorkspaceMockMvc.perform(get("/api/challenge-workspaces?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(challengeWorkspace.getId().intValue())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].termsAndConditions").value(hasItem(DEFAULT_TERMS_AND_CONDITIONS.toString())));
    }

    @Test
    @Transactional
    public void getChallengeWorkspace() throws Exception {
        // Initialize the database
        challengeWorkspaceRepository.saveAndFlush(challengeWorkspace);

        // Get the challengeWorkspace
        restChallengeWorkspaceMockMvc.perform(get("/api/challenge-workspaces/{id}", challengeWorkspace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(challengeWorkspace.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.termsAndConditions").value(DEFAULT_TERMS_AND_CONDITIONS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChallengeWorkspace() throws Exception {
        // Get the challengeWorkspace
        restChallengeWorkspaceMockMvc.perform(get("/api/challenge-workspaces/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChallengeWorkspace() throws Exception {
        // Initialize the database
        challengeWorkspaceRepository.saveAndFlush(challengeWorkspace);
        int databaseSizeBeforeUpdate = challengeWorkspaceRepository.findAll().size();

        // Update the challengeWorkspace
        ChallengeWorkspace updatedChallengeWorkspace = challengeWorkspaceRepository.findOne(challengeWorkspace.getId());
        updatedChallengeWorkspace
                .createdDate(UPDATED_CREATED_DATE)
                .termsAndConditions(UPDATED_TERMS_AND_CONDITIONS);

        restChallengeWorkspaceMockMvc.perform(put("/api/challenge-workspaces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChallengeWorkspace)))
                .andExpect(status().isOk());

        // Validate the ChallengeWorkspace in the database
        List<ChallengeWorkspace> challengeWorkspaces = challengeWorkspaceRepository.findAll();
        assertThat(challengeWorkspaces).hasSize(databaseSizeBeforeUpdate);
        ChallengeWorkspace testChallengeWorkspace = challengeWorkspaces.get(challengeWorkspaces.size() - 1);
        assertThat(testChallengeWorkspace.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testChallengeWorkspace.getTermsAndConditions()).isEqualTo(UPDATED_TERMS_AND_CONDITIONS);
    }

    @Test
    @Transactional
    public void deleteChallengeWorkspace() throws Exception {
        // Initialize the database
        challengeWorkspaceRepository.saveAndFlush(challengeWorkspace);
        int databaseSizeBeforeDelete = challengeWorkspaceRepository.findAll().size();

        // Get the challengeWorkspace
        restChallengeWorkspaceMockMvc.perform(delete("/api/challenge-workspaces/{id}", challengeWorkspace.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChallengeWorkspace> challengeWorkspaces = challengeWorkspaceRepository.findAll();
        assertThat(challengeWorkspaces).hasSize(databaseSizeBeforeDelete - 1);
    }
}
