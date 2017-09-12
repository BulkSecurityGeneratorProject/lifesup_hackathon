package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.UserInfo;
import fi.lifesup.hackathon.repository.UserInfoRepository;

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
 * Test class for the UserInfoResource REST controller.
 *
 * @see UserInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class UserInfoResourceIntTest {

    private static final String DEFAULT_INTRODUCTION = "AAAAA";
    private static final String UPDATED_INTRODUCTION = "BBBBB";

    private static final String DEFAULT_TWITTER_URL = "AAAAA";
    private static final String UPDATED_TWITTER_URL = "BBBBB";

    private static final String DEFAULT_LINKED_IN_URL = "AAAAA";
    private static final String UPDATED_LINKED_IN_URL = "BBBBB";

    private static final String DEFAULT_WEBSITE_URL = "AAAAA";
    private static final String UPDATED_WEBSITE_URL = "BBBBB";

    private static final String DEFAULT_SKILLS = "AAAAA";
    private static final String UPDATED_SKILLS = "BBBBB";

    private static final String DEFAULT_WORK_AREA = "AAAAA";
    private static final String UPDATED_WORK_AREA = "BBBBB";

    private static final String DEFAULT_FEEDBACK_FROM = "AAAAA";
    private static final String UPDATED_FEEDBACK_FROM = "BBBBB";

    @Inject
    private UserInfoRepository userInfoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserInfoMockMvc;

    private UserInfo userInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserInfoResource userInfoResource = new UserInfoResource();
        ReflectionTestUtils.setField(userInfoResource, "userInfoRepository", userInfoRepository);
        this.restUserInfoMockMvc = MockMvcBuilders.standaloneSetup(userInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInfo createEntity(EntityManager em) {
        UserInfo userInfo = new UserInfo()
                .introduction(DEFAULT_INTRODUCTION)
                .twitterUrl(DEFAULT_TWITTER_URL)
                .linkedInUrl(DEFAULT_LINKED_IN_URL)
                .websiteUrl(DEFAULT_WEBSITE_URL)
                .skills(DEFAULT_SKILLS)
                .workArea(DEFAULT_WORK_AREA)
                .feedbackFrom(DEFAULT_FEEDBACK_FROM);
        return userInfo;
    }

    @Before
    public void initTest() {
        userInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserInfo() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();

        // Create the UserInfo

        restUserInfoMockMvc.perform(post("/api/user-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInfo)))
                .andExpect(status().isCreated());

        // Validate the UserInfo in the database
        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeCreate + 1);
        UserInfo testUserInfo = userInfos.get(userInfos.size() - 1);
        assertThat(testUserInfo.getIntroduction()).isEqualTo(DEFAULT_INTRODUCTION);
        assertThat(testUserInfo.getTwitterUrl()).isEqualTo(DEFAULT_TWITTER_URL);
        assertThat(testUserInfo.getLinkedInUrl()).isEqualTo(DEFAULT_LINKED_IN_URL);
        assertThat(testUserInfo.getWebsiteUrl()).isEqualTo(DEFAULT_WEBSITE_URL);
        assertThat(testUserInfo.getSkills()).isEqualTo(DEFAULT_SKILLS);
        assertThat(testUserInfo.getWorkArea()).isEqualTo(DEFAULT_WORK_AREA);
        assertThat(testUserInfo.getFeedbackFrom()).isEqualTo(DEFAULT_FEEDBACK_FROM);
    }

    @Test
    @Transactional
    public void checkIntroductionIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setIntroduction(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInfo)))
                .andExpect(status().isBadRequest());

        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSkillsIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setSkills(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInfo)))
                .andExpect(status().isBadRequest());

        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWorkAreaIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setWorkArea(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInfo)))
                .andExpect(status().isBadRequest());

        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFeedbackFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setFeedbackFrom(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInfo)))
                .andExpect(status().isBadRequest());

        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserInfos() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfos
        restUserInfoMockMvc.perform(get("/api/user-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].introduction").value(hasItem(DEFAULT_INTRODUCTION.toString())))
                .andExpect(jsonPath("$.[*].twitterUrl").value(hasItem(DEFAULT_TWITTER_URL.toString())))
                .andExpect(jsonPath("$.[*].linkedInUrl").value(hasItem(DEFAULT_LINKED_IN_URL.toString())))
                .andExpect(jsonPath("$.[*].websiteUrl").value(hasItem(DEFAULT_WEBSITE_URL.toString())))
                .andExpect(jsonPath("$.[*].skills").value(hasItem(DEFAULT_SKILLS.toString())))
                .andExpect(jsonPath("$.[*].workArea").value(hasItem(DEFAULT_WORK_AREA.toString())))
                .andExpect(jsonPath("$.[*].feedbackFrom").value(hasItem(DEFAULT_FEEDBACK_FROM.toString())));
    }

    @Test
    @Transactional
    public void getUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", userInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userInfo.getId().intValue()))
            .andExpect(jsonPath("$.introduction").value(DEFAULT_INTRODUCTION.toString()))
            .andExpect(jsonPath("$.twitterUrl").value(DEFAULT_TWITTER_URL.toString()))
            .andExpect(jsonPath("$.linkedInUrl").value(DEFAULT_LINKED_IN_URL.toString()))
            .andExpect(jsonPath("$.websiteUrl").value(DEFAULT_WEBSITE_URL.toString()))
            .andExpect(jsonPath("$.skills").value(DEFAULT_SKILLS.toString()))
            .andExpect(jsonPath("$.workArea").value(DEFAULT_WORK_AREA.toString()))
            .andExpect(jsonPath("$.feedbackFrom").value(DEFAULT_FEEDBACK_FROM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserInfo() throws Exception {
        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Update the userInfo
        UserInfo updatedUserInfo = userInfoRepository.findOne(userInfo.getId());
        updatedUserInfo
                .introduction(UPDATED_INTRODUCTION)
                .twitterUrl(UPDATED_TWITTER_URL)
                .linkedInUrl(UPDATED_LINKED_IN_URL)
                .websiteUrl(UPDATED_WEBSITE_URL)
                .skills(UPDATED_SKILLS)
                .workArea(UPDATED_WORK_AREA)
                .feedbackFrom(UPDATED_FEEDBACK_FROM);

        restUserInfoMockMvc.perform(put("/api/user-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserInfo)))
                .andExpect(status().isOk());

        // Validate the UserInfo in the database
        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeUpdate);
        UserInfo testUserInfo = userInfos.get(userInfos.size() - 1);
        assertThat(testUserInfo.getIntroduction()).isEqualTo(UPDATED_INTRODUCTION);
        assertThat(testUserInfo.getTwitterUrl()).isEqualTo(UPDATED_TWITTER_URL);
        assertThat(testUserInfo.getLinkedInUrl()).isEqualTo(UPDATED_LINKED_IN_URL);
        assertThat(testUserInfo.getWebsiteUrl()).isEqualTo(UPDATED_WEBSITE_URL);
        assertThat(testUserInfo.getSkills()).isEqualTo(UPDATED_SKILLS);
        assertThat(testUserInfo.getWorkArea()).isEqualTo(UPDATED_WORK_AREA);
        assertThat(testUserInfo.getFeedbackFrom()).isEqualTo(UPDATED_FEEDBACK_FROM);
    }

    @Test
    @Transactional
    public void deleteUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);
        int databaseSizeBeforeDelete = userInfoRepository.findAll().size();

        // Get the userInfo
        restUserInfoMockMvc.perform(delete("/api/user-infos/{id}", userInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
