package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.GoCloudJApp;
import com.cloud7works.gocloud.domain.CloudMigration;
import com.cloud7works.gocloud.repository.CloudMigrationRepository;
import com.cloud7works.gocloud.repository.search.CloudMigrationSearchRepository;
import com.cloud7works.gocloud.service.CloudMigrationService;
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
 * Integration tests for the {@Link CloudMigrationResource} REST controller.
 */
@SpringBootTest(classes = GoCloudJApp.class)
public class CloudMigrationResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CLOUD_MIGRATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLOUD_MIGRATION_ID = "BBBBBBBBBB";

    @Autowired
    private CloudMigrationRepository cloudMigrationRepository;

    @Autowired
    private CloudMigrationService cloudMigrationService;

    /**
     * This repository is mocked in the com.cloud7works.gocloud.repository.search test package.
     *
     * @see com.cloud7works.gocloud.repository.search.CloudMigrationSearchRepositoryMockConfiguration
     */
    @Autowired
    private CloudMigrationSearchRepository mockCloudMigrationSearchRepository;

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

    private MockMvc restCloudMigrationMockMvc;

    private CloudMigration cloudMigration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CloudMigrationResource cloudMigrationResource = new CloudMigrationResource(cloudMigrationService);
        this.restCloudMigrationMockMvc = MockMvcBuilders.standaloneSetup(cloudMigrationResource)
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
    public static CloudMigration createEntity(EntityManager em) {
        CloudMigration cloudMigration = new CloudMigration()
            .userId(DEFAULT_USER_ID)
            .cloudMigrationId(DEFAULT_CLOUD_MIGRATION_ID);
        return cloudMigration;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CloudMigration createUpdatedEntity(EntityManager em) {
        CloudMigration cloudMigration = new CloudMigration()
            .userId(UPDATED_USER_ID)
            .cloudMigrationId(UPDATED_CLOUD_MIGRATION_ID);
        return cloudMigration;
    }

    @BeforeEach
    public void initTest() {
        cloudMigration = createEntity(em);
    }

    @Test
    @Transactional
    public void createCloudMigration() throws Exception {
        int databaseSizeBeforeCreate = cloudMigrationRepository.findAll().size();

        // Create the CloudMigration
        restCloudMigrationMockMvc.perform(post("/api/cloud-migrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cloudMigration)))
            .andExpect(status().isCreated());

        // Validate the CloudMigration in the database
        List<CloudMigration> cloudMigrationList = cloudMigrationRepository.findAll();
        assertThat(cloudMigrationList).hasSize(databaseSizeBeforeCreate + 1);
        CloudMigration testCloudMigration = cloudMigrationList.get(cloudMigrationList.size() - 1);
        assertThat(testCloudMigration.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testCloudMigration.getCloudMigrationId()).isEqualTo(DEFAULT_CLOUD_MIGRATION_ID);

        // Validate the CloudMigration in Elasticsearch
        verify(mockCloudMigrationSearchRepository, times(1)).save(testCloudMigration);
    }

    @Test
    @Transactional
    public void createCloudMigrationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cloudMigrationRepository.findAll().size();

        // Create the CloudMigration with an existing ID
        cloudMigration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCloudMigrationMockMvc.perform(post("/api/cloud-migrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cloudMigration)))
            .andExpect(status().isBadRequest());

        // Validate the CloudMigration in the database
        List<CloudMigration> cloudMigrationList = cloudMigrationRepository.findAll();
        assertThat(cloudMigrationList).hasSize(databaseSizeBeforeCreate);

        // Validate the CloudMigration in Elasticsearch
        verify(mockCloudMigrationSearchRepository, times(0)).save(cloudMigration);
    }


    @Test
    @Transactional
    public void getAllCloudMigrations() throws Exception {
        // Initialize the database
        cloudMigrationRepository.saveAndFlush(cloudMigration);

        // Get all the cloudMigrationList
        restCloudMigrationMockMvc.perform(get("/api/cloud-migrations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cloudMigration.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].cloudMigrationId").value(hasItem(DEFAULT_CLOUD_MIGRATION_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getCloudMigration() throws Exception {
        // Initialize the database
        cloudMigrationRepository.saveAndFlush(cloudMigration);

        // Get the cloudMigration
        restCloudMigrationMockMvc.perform(get("/api/cloud-migrations/{id}", cloudMigration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cloudMigration.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.cloudMigrationId").value(DEFAULT_CLOUD_MIGRATION_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCloudMigration() throws Exception {
        // Get the cloudMigration
        restCloudMigrationMockMvc.perform(get("/api/cloud-migrations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCloudMigration() throws Exception {
        // Initialize the database
        cloudMigrationService.save(cloudMigration);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCloudMigrationSearchRepository);

        int databaseSizeBeforeUpdate = cloudMigrationRepository.findAll().size();

        // Update the cloudMigration
        CloudMigration updatedCloudMigration = cloudMigrationRepository.findById(cloudMigration.getId()).get();
        // Disconnect from session so that the updates on updatedCloudMigration are not directly saved in db
        em.detach(updatedCloudMigration);
        updatedCloudMigration
            .userId(UPDATED_USER_ID)
            .cloudMigrationId(UPDATED_CLOUD_MIGRATION_ID);

        restCloudMigrationMockMvc.perform(put("/api/cloud-migrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCloudMigration)))
            .andExpect(status().isOk());

        // Validate the CloudMigration in the database
        List<CloudMigration> cloudMigrationList = cloudMigrationRepository.findAll();
        assertThat(cloudMigrationList).hasSize(databaseSizeBeforeUpdate);
        CloudMigration testCloudMigration = cloudMigrationList.get(cloudMigrationList.size() - 1);
        assertThat(testCloudMigration.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testCloudMigration.getCloudMigrationId()).isEqualTo(UPDATED_CLOUD_MIGRATION_ID);

        // Validate the CloudMigration in Elasticsearch
        verify(mockCloudMigrationSearchRepository, times(1)).save(testCloudMigration);
    }

    @Test
    @Transactional
    public void updateNonExistingCloudMigration() throws Exception {
        int databaseSizeBeforeUpdate = cloudMigrationRepository.findAll().size();

        // Create the CloudMigration

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCloudMigrationMockMvc.perform(put("/api/cloud-migrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cloudMigration)))
            .andExpect(status().isBadRequest());

        // Validate the CloudMigration in the database
        List<CloudMigration> cloudMigrationList = cloudMigrationRepository.findAll();
        assertThat(cloudMigrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CloudMigration in Elasticsearch
        verify(mockCloudMigrationSearchRepository, times(0)).save(cloudMigration);
    }

    @Test
    @Transactional
    public void deleteCloudMigration() throws Exception {
        // Initialize the database
        cloudMigrationService.save(cloudMigration);

        int databaseSizeBeforeDelete = cloudMigrationRepository.findAll().size();

        // Delete the cloudMigration
        restCloudMigrationMockMvc.perform(delete("/api/cloud-migrations/{id}", cloudMigration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<CloudMigration> cloudMigrationList = cloudMigrationRepository.findAll();
        assertThat(cloudMigrationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CloudMigration in Elasticsearch
        verify(mockCloudMigrationSearchRepository, times(1)).deleteById(cloudMigration.getId());
    }

    @Test
    @Transactional
    public void searchCloudMigration() throws Exception {
        // Initialize the database
        cloudMigrationService.save(cloudMigration);
        when(mockCloudMigrationSearchRepository.search(queryStringQuery("id:" + cloudMigration.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cloudMigration), PageRequest.of(0, 1), 1));
        // Search the cloudMigration
        restCloudMigrationMockMvc.perform(get("/api/_search/cloud-migrations?query=id:" + cloudMigration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cloudMigration.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].cloudMigrationId").value(hasItem(DEFAULT_CLOUD_MIGRATION_ID)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CloudMigration.class);
        CloudMigration cloudMigration1 = new CloudMigration();
        cloudMigration1.setId(1L);
        CloudMigration cloudMigration2 = new CloudMigration();
        cloudMigration2.setId(cloudMigration1.getId());
        assertThat(cloudMigration1).isEqualTo(cloudMigration2);
        cloudMigration2.setId(2L);
        assertThat(cloudMigration1).isNotEqualTo(cloudMigration2);
        cloudMigration1.setId(null);
        assertThat(cloudMigration1).isNotEqualTo(cloudMigration2);
    }
}
