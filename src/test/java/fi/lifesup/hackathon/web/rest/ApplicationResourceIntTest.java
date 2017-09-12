package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.repository.ApplicationRepository;

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

import fi.lifesup.hackathon.domain.enumeration.ApplicationStatus;
/**
 * Test class for the ApplicationResource REST controller.
 *
 * @see ApplicationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class ApplicationResourceIntTest {

    private static final String DEFAULT_TEAM_NAME = "AAAAA";
    private static final String UPDATED_TEAM_NAME = "BBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final String DEFAULT_MOTIVATION = "AAAAA";
    private static final String UPDATED_MOTIVATION = "BBBBB";

    private static final String DEFAULT_IDEAS_DESCRIPTION = "AAAAA";
    private static final String UPDATED_IDEAS_DESCRIPTION = "BBBBB";

    private static final ApplicationStatus DEFAULT_STATUS = ApplicationStatus.DRAFT;
    private static final ApplicationStatus UPDATED_STATUS = ApplicationStatus.WAITING_FOR_APPROVE;

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restApplicationMockMvc;

    private Application application;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApplicationResource applicationResource = new ApplicationResource();
        ReflectionTestUtils.setField(applicationResource, "applicationRepository", applicationRepository);
        this.restApplicationMockMvc = MockMvcBuilders.standaloneSetup(applicationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createEntity(EntityManager em) {
        Application application = new Application()
                .teamName(DEFAULT_TEAM_NAME)
                .companyName(DEFAULT_COMPANY_NAME)
                .description(DEFAULT_DESCRIPTION)
                .motivation(DEFAULT_MOTIVATION)
                .ideasDescription(DEFAULT_IDEAS_DESCRIPTION)
                .status(DEFAULT_STATUS);
        return application;
    }

    @Before
    public void initTest() {
        application = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplication() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // Create the Application

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applications.get(applications.size() - 1);
        assertThat(testApplication.getTeamName()).isEqualTo(DEFAULT_TEAM_NAME);
        assertThat(testApplication.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testApplication.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApplication.getMotivation()).isEqualTo(DEFAULT_MOTIVATION);
        assertThat(testApplication.getIdeasDescription()).isEqualTo(DEFAULT_IDEAS_DESCRIPTION);
        assertThat(testApplication.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkTeamNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setTeamName(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setDescription(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMotivationIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setMotivation(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdeasDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setIdeasDescription(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applications
        restApplicationMockMvc.perform(get("/api/applications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
                .andExpect(jsonPath("$.[*].teamName").value(hasItem(DEFAULT_TEAM_NAME.toString())))
                .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].motivation").value(hasItem(DEFAULT_MOTIVATION.toString())))
                .andExpect(jsonPath("$.[*].ideasDescription").value(hasItem(DEFAULT_IDEAS_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(application.getId().intValue()))
            .andExpect(jsonPath("$.teamName").value(DEFAULT_TEAM_NAME.toString()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.motivation").value(DEFAULT_MOTIVATION.toString()))
            .andExpect(jsonPath("$.ideasDescription").value(DEFAULT_IDEAS_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApplication() throws Exception {
        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application
        Application updatedApplication = applicationRepository.findOne(application.getId());
        updatedApplication
                .teamName(UPDATED_TEAM_NAME)
                .companyName(UPDATED_COMPANY_NAME)
                .description(UPDATED_DESCRIPTION)
                .motivation(UPDATED_MOTIVATION)
                .ideasDescription(UPDATED_IDEAS_DESCRIPTION)
                .status(UPDATED_STATUS);

        restApplicationMockMvc.perform(put("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedApplication)))
                .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applications.get(applications.size() - 1);
        assertThat(testApplication.getTeamName()).isEqualTo(UPDATED_TEAM_NAME);
        assertThat(testApplication.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testApplication.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplication.getMotivation()).isEqualTo(UPDATED_MOTIVATION);
        assertThat(testApplication.getIdeasDescription()).isEqualTo(UPDATED_IDEAS_DESCRIPTION);
        assertThat(testApplication.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        int databaseSizeBeforeDelete = applicationRepository.findAll().size();

        // Get the application
        restApplicationMockMvc.perform(delete("/api/applications/{id}", application.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
