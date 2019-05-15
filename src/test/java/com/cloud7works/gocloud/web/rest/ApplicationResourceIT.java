package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.GoCloudJApp;
import com.cloud7works.gocloud.domain.Application;
import com.cloud7works.gocloud.repository.ApplicationRepository;
import com.cloud7works.gocloud.repository.search.ApplicationSearchRepository;
import com.cloud7works.gocloud.service.ApplicationService;
import com.cloud7works.gocloud.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import com.cloud7works.gocloud.domain.enumeration.ApplicationType;
import com.cloud7works.gocloud.domain.enumeration.Environment;
/**
 * Integration tests for the {@Link ApplicationResource} REST controller.
 */
@SpringBootTest(classes = GoCloudJApp.class)
public class ApplicationResourceIT {

    private static final String DEFAULT_APPLICATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_ID = "BBBBBBBBBB";

    private static final ApplicationType DEFAULT_TYPE = ApplicationType.WEB;
    private static final ApplicationType UPDATED_TYPE = ApplicationType.DATABASE;

    private static final String DEFAULT_GIT_URL = "AAAAAAAAAA";
    private static final String UPDATED_GIT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_STACK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STACK_NAME = "BBBBBBBBBB";

    private static final Environment DEFAULT_ENVIRONMENT = Environment.DEV;
    private static final Environment UPDATED_ENVIRONMENT = Environment.SIT;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    /**
     * This repository is mocked in the com.cloud7works.gocloud.repository.search test package.
     *
     * @see com.cloud7works.gocloud.repository.search.ApplicationSearchRepositoryMockConfiguration
     */
    @Autowired
    private ApplicationSearchRepository mockApplicationSearchRepository;

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

    private MockMvc restApplicationMockMvc;

    private Application application;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicationResource applicationResource = new ApplicationResource(applicationService);
        this.restApplicationMockMvc = MockMvcBuilders.standaloneSetup(applicationResource)
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
    public static Application createEntity(EntityManager em) {
        Application application = new Application()
            .applicationId(DEFAULT_APPLICATION_ID)
            .type(DEFAULT_TYPE)
            .gitUrl(DEFAULT_GIT_URL)
            .stackName(DEFAULT_STACK_NAME)
            .environment(DEFAULT_ENVIRONMENT);
        return application;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createUpdatedEntity(EntityManager em) {
        Application application = new Application()
            .applicationId(UPDATED_APPLICATION_ID)
            .type(UPDATED_TYPE)
            .gitUrl(UPDATED_GIT_URL)
            .stackName(UPDATED_STACK_NAME)
            .environment(UPDATED_ENVIRONMENT);
        return application;
    }

    @BeforeEach
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
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getApplicationId()).isEqualTo(DEFAULT_APPLICATION_ID);
        assertThat(testApplication.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApplication.getGitUrl()).isEqualTo(DEFAULT_GIT_URL);
        assertThat(testApplication.getStackName()).isEqualTo(DEFAULT_STACK_NAME);
        assertThat(testApplication.getEnvironment()).isEqualTo(DEFAULT_ENVIRONMENT);

        // Validate the Application in Elasticsearch
        verify(mockApplicationSearchRepository, times(1)).save(testApplication);
    }

    @Test
    @Transactional
    public void createApplicationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // Create the Application with an existing ID
        application.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Application in Elasticsearch
        verify(mockApplicationSearchRepository, times(0)).save(application);
    }


    @Test
    @Transactional
    public void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList
        restApplicationMockMvc.perform(get("/api/applications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationId").value(hasItem(DEFAULT_APPLICATION_ID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].gitUrl").value(hasItem(DEFAULT_GIT_URL.toString())))
            .andExpect(jsonPath("$.[*].stackName").value(hasItem(DEFAULT_STACK_NAME.toString())))
            .andExpect(jsonPath("$.[*].environment").value(hasItem(DEFAULT_ENVIRONMENT.toString())));
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
            .andExpect(jsonPath("$.applicationId").value(DEFAULT_APPLICATION_ID.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.gitUrl").value(DEFAULT_GIT_URL.toString()))
            .andExpect(jsonPath("$.stackName").value(DEFAULT_STACK_NAME.toString()))
            .andExpect(jsonPath("$.environment").value(DEFAULT_ENVIRONMENT.toString()));
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
        applicationService.save(application);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockApplicationSearchRepository);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application
        Application updatedApplication = applicationRepository.findById(application.getId()).get();
        // Disconnect from session so that the updates on updatedApplication are not directly saved in db
        em.detach(updatedApplication);
        updatedApplication
            .applicationId(UPDATED_APPLICATION_ID)
            .type(UPDATED_TYPE)
            .gitUrl(UPDATED_GIT_URL)
            .stackName(UPDATED_STACK_NAME)
            .environment(UPDATED_ENVIRONMENT);

        restApplicationMockMvc.perform(put("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplication)))
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getApplicationId()).isEqualTo(UPDATED_APPLICATION_ID);
        assertThat(testApplication.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApplication.getGitUrl()).isEqualTo(UPDATED_GIT_URL);
        assertThat(testApplication.getStackName()).isEqualTo(UPDATED_STACK_NAME);
        assertThat(testApplication.getEnvironment()).isEqualTo(UPDATED_ENVIRONMENT);

        // Validate the Application in Elasticsearch
        verify(mockApplicationSearchRepository, times(1)).save(testApplication);
    }

    @Test
    @Transactional
    public void updateNonExistingApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Create the Application

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMockMvc.perform(put("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Application in Elasticsearch
        verify(mockApplicationSearchRepository, times(0)).save(application);
    }

    @Test
    @Transactional
    public void deleteApplication() throws Exception {
        // Initialize the database
        applicationService.save(application);

        int databaseSizeBeforeDelete = applicationRepository.findAll().size();

        // Delete the application
        restApplicationMockMvc.perform(delete("/api/applications/{id}", application.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Application in Elasticsearch
        verify(mockApplicationSearchRepository, times(1)).deleteById(application.getId());
    }

    @Test
    @Transactional
    public void searchApplication() throws Exception {
        // Initialize the database
        applicationService.save(application);
        when(mockApplicationSearchRepository.search(queryStringQuery("id:" + application.getId())))
            .thenReturn(Collections.singletonList(application));
        // Search the application
        restApplicationMockMvc.perform(get("/api/_search/applications?query=id:" + application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationId").value(hasItem(DEFAULT_APPLICATION_ID)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].gitUrl").value(hasItem(DEFAULT_GIT_URL)))
            .andExpect(jsonPath("$.[*].stackName").value(hasItem(DEFAULT_STACK_NAME)))
            .andExpect(jsonPath("$.[*].environment").value(hasItem(DEFAULT_ENVIRONMENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Application.class);
        Application application1 = new Application();
        application1.setId(1L);
        Application application2 = new Application();
        application2.setId(application1.getId());
        assertThat(application1).isEqualTo(application2);
        application2.setId(2L);
        assertThat(application1).isNotEqualTo(application2);
        application1.setId(null);
        assertThat(application1).isNotEqualTo(application2);
    }
}
