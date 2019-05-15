package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.GoCloudJApp;
import com.cloud7works.gocloud.domain.GoCloudUser;
import com.cloud7works.gocloud.repository.GoCloudUserRepository;
import com.cloud7works.gocloud.repository.search.GoCloudUserSearchRepository;
import com.cloud7works.gocloud.service.GoCloudUserService;
import com.cloud7works.gocloud.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.cloud7works.gocloud.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link GoCloudUserResource} REST controller.
 */
@SpringBootTest(classes = GoCloudJApp.class)
public class GoCloudUserResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLOUD_OR_NOT = "AAAAAAAAAA";
    private static final String UPDATED_CLOUD_OR_NOT = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private GoCloudUserRepository goCloudUserRepository;

    @Autowired
    private GoCloudUserService goCloudUserService;

    /**
     * This repository is mocked in the com.cloud7works.gocloud.repository.search test package.
     *
     * @see com.cloud7works.gocloud.repository.search.GoCloudUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private GoCloudUserSearchRepository mockGoCloudUserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restGoCloudUserMockMvc;

    private GoCloudUser goCloudUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GoCloudUserResource goCloudUserResource = new GoCloudUserResource(goCloudUserService);
        this.restGoCloudUserMockMvc = MockMvcBuilders.standaloneSetup(goCloudUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoCloudUser createEntity(EntityManager em) {
        GoCloudUser goCloudUser = new GoCloudUser()
            .companyName(DEFAULT_COMPANY_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .cloudOrNot(DEFAULT_CLOUD_OR_NOT)
            .userId(DEFAULT_USER_ID)
            .email(DEFAULT_EMAIL);
        return goCloudUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoCloudUser createUpdatedEntity(EntityManager em) {
        GoCloudUser goCloudUser = new GoCloudUser()
            .companyName(UPDATED_COMPANY_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .cloudOrNot(UPDATED_CLOUD_OR_NOT)
            .userId(UPDATED_USER_ID)
            .email(UPDATED_EMAIL);
        return goCloudUser;
    }

    @BeforeEach
    public void initTest() {
        goCloudUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createGoCloudUser() throws Exception {
        int databaseSizeBeforeCreate = goCloudUserRepository.findAll().size();

        // Create the GoCloudUser
        restGoCloudUserMockMvc.perform(post("/api/go-cloud-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goCloudUser)))
            .andExpect(status().isCreated());

        // Validate the GoCloudUser in the database
        List<GoCloudUser> goCloudUserList = goCloudUserRepository.findAll();
        assertThat(goCloudUserList).hasSize(databaseSizeBeforeCreate + 1);
        GoCloudUser testGoCloudUser = goCloudUserList.get(goCloudUserList.size() - 1);
        assertThat(testGoCloudUser.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testGoCloudUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testGoCloudUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testGoCloudUser.getCloudOrNot()).isEqualTo(DEFAULT_CLOUD_OR_NOT);
        assertThat(testGoCloudUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testGoCloudUser.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the GoCloudUser in Elasticsearch
        verify(mockGoCloudUserSearchRepository, times(1)).save(testGoCloudUser);
    }

    @Test
    @Transactional
    public void createGoCloudUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = goCloudUserRepository.findAll().size();

        // Create the GoCloudUser with an existing ID
        goCloudUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoCloudUserMockMvc.perform(post("/api/go-cloud-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goCloudUser)))
            .andExpect(status().isBadRequest());

        // Validate the GoCloudUser in the database
        List<GoCloudUser> goCloudUserList = goCloudUserRepository.findAll();
        assertThat(goCloudUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the GoCloudUser in Elasticsearch
        verify(mockGoCloudUserSearchRepository, times(0)).save(goCloudUser);
    }


    @Test
    @Transactional
    public void getAllGoCloudUsers() throws Exception {
        // Initialize the database
        goCloudUserRepository.saveAndFlush(goCloudUser);

        // Get all the goCloudUserList
        restGoCloudUserMockMvc.perform(get("/api/go-cloud-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goCloudUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].cloudOrNot").value(hasItem(DEFAULT_CLOUD_OR_NOT.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
    
    @Test
    @Transactional
    public void getGoCloudUser() throws Exception {
        // Initialize the database
        goCloudUserRepository.saveAndFlush(goCloudUser);

        // Get the goCloudUser
        restGoCloudUserMockMvc.perform(get("/api/go-cloud-users/{id}", goCloudUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(goCloudUser.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.cloudOrNot").value(DEFAULT_CLOUD_OR_NOT.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGoCloudUser() throws Exception {
        // Get the goCloudUser
        restGoCloudUserMockMvc.perform(get("/api/go-cloud-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoCloudUser() throws Exception {
        // Initialize the database
        goCloudUserService.save(goCloudUser);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockGoCloudUserSearchRepository);

        int databaseSizeBeforeUpdate = goCloudUserRepository.findAll().size();

        // Update the goCloudUser
        GoCloudUser updatedGoCloudUser = goCloudUserRepository.findById(goCloudUser.getId()).get();
        // Disconnect from session so that the updates on updatedGoCloudUser are not directly saved in db
        em.detach(updatedGoCloudUser);
        updatedGoCloudUser
            .companyName(UPDATED_COMPANY_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .cloudOrNot(UPDATED_CLOUD_OR_NOT)
            .userId(UPDATED_USER_ID)
            .email(UPDATED_EMAIL);

        restGoCloudUserMockMvc.perform(put("/api/go-cloud-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGoCloudUser)))
            .andExpect(status().isOk());

        // Validate the GoCloudUser in the database
        List<GoCloudUser> goCloudUserList = goCloudUserRepository.findAll();
        assertThat(goCloudUserList).hasSize(databaseSizeBeforeUpdate);
        GoCloudUser testGoCloudUser = goCloudUserList.get(goCloudUserList.size() - 1);
        assertThat(testGoCloudUser.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testGoCloudUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testGoCloudUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testGoCloudUser.getCloudOrNot()).isEqualTo(UPDATED_CLOUD_OR_NOT);
        assertThat(testGoCloudUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testGoCloudUser.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the GoCloudUser in Elasticsearch
        verify(mockGoCloudUserSearchRepository, times(1)).save(testGoCloudUser);
    }

    @Test
    @Transactional
    public void updateNonExistingGoCloudUser() throws Exception {
        int databaseSizeBeforeUpdate = goCloudUserRepository.findAll().size();

        // Create the GoCloudUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoCloudUserMockMvc.perform(put("/api/go-cloud-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goCloudUser)))
            .andExpect(status().isBadRequest());

        // Validate the GoCloudUser in the database
        List<GoCloudUser> goCloudUserList = goCloudUserRepository.findAll();
        assertThat(goCloudUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GoCloudUser in Elasticsearch
        verify(mockGoCloudUserSearchRepository, times(0)).save(goCloudUser);
    }

    @Test
    @Transactional
    public void deleteGoCloudUser() throws Exception {
        // Initialize the database
        goCloudUserService.save(goCloudUser);

        int databaseSizeBeforeDelete = goCloudUserRepository.findAll().size();

        // Delete the goCloudUser
        restGoCloudUserMockMvc.perform(delete("/api/go-cloud-users/{id}", goCloudUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<GoCloudUser> goCloudUserList = goCloudUserRepository.findAll();
        assertThat(goCloudUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GoCloudUser in Elasticsearch
        verify(mockGoCloudUserSearchRepository, times(1)).deleteById(goCloudUser.getId());
    }

    @Test
    @Transactional
    public void searchGoCloudUser() throws Exception {
        // Initialize the database
        goCloudUserService.save(goCloudUser);
        when(mockGoCloudUserSearchRepository.search(queryStringQuery("id:" + goCloudUser.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(goCloudUser), PageRequest.of(0, 1), 1));
        // Search the goCloudUser
        restGoCloudUserMockMvc.perform(get("/api/_search/go-cloud-users?query=id:" + goCloudUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goCloudUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].cloudOrNot").value(hasItem(DEFAULT_CLOUD_OR_NOT)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoCloudUser.class);
        GoCloudUser goCloudUser1 = new GoCloudUser();
        goCloudUser1.setId(1L);
        GoCloudUser goCloudUser2 = new GoCloudUser();
        goCloudUser2.setId(goCloudUser1.getId());
        assertThat(goCloudUser1).isEqualTo(goCloudUser2);
        goCloudUser2.setId(2L);
        assertThat(goCloudUser1).isNotEqualTo(goCloudUser2);
        goCloudUser1.setId(null);
        assertThat(goCloudUser1).isNotEqualTo(goCloudUser2);
    }
}
