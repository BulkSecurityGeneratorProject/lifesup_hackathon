package fi.lifesup.hackathon.web.rest;

import fi.lifesup.hackathon.HackathonApp;

import fi.lifesup.hackathon.domain.UserList;
import fi.lifesup.hackathon.repository.UserListRepository;

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

import fi.lifesup.hackathon.domain.enumeration.UserSex;
import fi.lifesup.hackathon.domain.enumeration.UserStatus;
/**
 * Test class for the UserListResource REST controller.
 *
 * @see UserListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApp.class)
public class UserListResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAA";
    private static final String UPDATED_PASSWORD = "BBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBB";

    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";

    private static final UserSex DEFAULT_SEX = UserSex.MALE;
    private static final UserSex UPDATED_SEX = UserSex.FEMALE;

    private static final String DEFAULT_COMPANY_NAME = "AAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBB";

    private static final String DEFAULT_LOGO_URL = "AAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAA";
    private static final String UPDATED_COUNTRY = "BBBBB";

    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBB";

    private static final ZonedDateTime DEFAULT_BIRTHDAY = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_BIRTHDAY = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_BIRTHDAY_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_BIRTHDAY);

    private static final UserStatus DEFAULT_STATUS = UserStatus.INACTIVATED;
    private static final UserStatus UPDATED_STATUS = UserStatus.ACTIVATED;

    @Inject
    private UserListRepository userListRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserListMockMvc;

    private UserList userList;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserListResource userListResource = new UserListResource();
        ReflectionTestUtils.setField(userListResource, "userListRepository", userListRepository);
        this.restUserListMockMvc = MockMvcBuilders.standaloneSetup(userListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserList createEntity(EntityManager em) {
        UserList userList = new UserList()
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .fullName(DEFAULT_FULL_NAME)
                .phone(DEFAULT_PHONE)
                .sex(DEFAULT_SEX)
                .companyName(DEFAULT_COMPANY_NAME)
                .jobTitle(DEFAULT_JOB_TITLE)
                .logoUrl(DEFAULT_LOGO_URL)
                .country(DEFAULT_COUNTRY)
                .city(DEFAULT_CITY)
                .nationality(DEFAULT_NATIONALITY)
                .birthday(DEFAULT_BIRTHDAY)
                .status(DEFAULT_STATUS);
        return userList;
    }

    @Before
    public void initTest() {
        userList = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserList() throws Exception {
        int databaseSizeBeforeCreate = userListRepository.findAll().size();

        // Create the UserList

        restUserListMockMvc.perform(post("/api/user-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userList)))
                .andExpect(status().isCreated());

        // Validate the UserList in the database
        List<UserList> userLists = userListRepository.findAll();
        assertThat(userLists).hasSize(databaseSizeBeforeCreate + 1);
        UserList testUserList = userLists.get(userLists.size() - 1);
        assertThat(testUserList.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserList.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUserList.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testUserList.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserList.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testUserList.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testUserList.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testUserList.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);
        assertThat(testUserList.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testUserList.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserList.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
        assertThat(testUserList.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testUserList.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = userListRepository.findAll().size();
        // set the field null
        userList.setEmail(null);

        // Create the UserList, which fails.

        restUserListMockMvc.perform(post("/api/user-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userList)))
                .andExpect(status().isBadRequest());

        List<UserList> userLists = userListRepository.findAll();
        assertThat(userLists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userListRepository.findAll().size();
        // set the field null
        userList.setFullName(null);

        // Create the UserList, which fails.

        restUserListMockMvc.perform(post("/api/user-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userList)))
                .andExpect(status().isBadRequest());

        List<UserList> userLists = userListRepository.findAll();
        assertThat(userLists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = userListRepository.findAll().size();
        // set the field null
        userList.setPhone(null);

        // Create the UserList, which fails.

        restUserListMockMvc.perform(post("/api/user-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userList)))
                .andExpect(status().isBadRequest());

        List<UserList> userLists = userListRepository.findAll();
        assertThat(userLists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSexIsRequired() throws Exception {
        int databaseSizeBeforeTest = userListRepository.findAll().size();
        // set the field null
        userList.setSex(null);

        // Create the UserList, which fails.

        restUserListMockMvc.perform(post("/api/user-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userList)))
                .andExpect(status().isBadRequest());

        List<UserList> userLists = userListRepository.findAll();
        assertThat(userLists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = userListRepository.findAll().size();
        // set the field null
        userList.setCountry(null);

        // Create the UserList, which fails.

        restUserListMockMvc.perform(post("/api/user-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userList)))
                .andExpect(status().isBadRequest());

        List<UserList> userLists = userListRepository.findAll();
        assertThat(userLists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = userListRepository.findAll().size();
        // set the field null
        userList.setCity(null);

        // Create the UserList, which fails.

        restUserListMockMvc.perform(post("/api/user-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userList)))
                .andExpect(status().isBadRequest());

        List<UserList> userLists = userListRepository.findAll();
        assertThat(userLists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNationalityIsRequired() throws Exception {
        int databaseSizeBeforeTest = userListRepository.findAll().size();
        // set the field null
        userList.setNationality(null);

        // Create the UserList, which fails.

        restUserListMockMvc.perform(post("/api/user-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userList)))
                .andExpect(status().isBadRequest());

        List<UserList> userLists = userListRepository.findAll();
        assertThat(userLists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserLists() throws Exception {
        // Initialize the database
        userListRepository.saveAndFlush(userList);

        // Get all the userLists
        restUserListMockMvc.perform(get("/api/user-lists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userList.getId().intValue())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
                .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
                .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
                .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())))
                .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getUserList() throws Exception {
        // Initialize the database
        userListRepository.saveAndFlush(userList);

        // Get the userList
        restUserListMockMvc.perform(get("/api/user-lists/{id}", userList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userList.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE.toString()))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY.toString()))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserList() throws Exception {
        // Get the userList
        restUserListMockMvc.perform(get("/api/user-lists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserList() throws Exception {
        // Initialize the database
        userListRepository.saveAndFlush(userList);
        int databaseSizeBeforeUpdate = userListRepository.findAll().size();

        // Update the userList
        UserList updatedUserList = userListRepository.findOne(userList.getId());
        updatedUserList
                .email(UPDATED_EMAIL)
                .password(UPDATED_PASSWORD)
                .fullName(UPDATED_FULL_NAME)
                .phone(UPDATED_PHONE)
                .sex(UPDATED_SEX)
                .companyName(UPDATED_COMPANY_NAME)
                .jobTitle(UPDATED_JOB_TITLE)
                .logoUrl(UPDATED_LOGO_URL)
                .country(UPDATED_COUNTRY)
                .city(UPDATED_CITY)
                .nationality(UPDATED_NATIONALITY)
                .birthday(UPDATED_BIRTHDAY)
                .status(UPDATED_STATUS);

        restUserListMockMvc.perform(put("/api/user-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserList)))
                .andExpect(status().isOk());

        // Validate the UserList in the database
        List<UserList> userLists = userListRepository.findAll();
        assertThat(userLists).hasSize(databaseSizeBeforeUpdate);
        UserList testUserList = userLists.get(userLists.size() - 1);
        assertThat(testUserList.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserList.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUserList.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testUserList.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserList.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testUserList.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testUserList.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testUserList.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testUserList.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserList.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserList.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testUserList.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testUserList.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteUserList() throws Exception {
        // Initialize the database
        userListRepository.saveAndFlush(userList);
        int databaseSizeBeforeDelete = userListRepository.findAll().size();

        // Get the userList
        restUserListMockMvc.perform(delete("/api/user-lists/{id}", userList.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserList> userLists = userListRepository.findAll();
        assertThat(userLists).hasSize(databaseSizeBeforeDelete - 1);
    }
}
