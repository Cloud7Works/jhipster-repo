package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.GoCloudJApp;
import com.cloud7works.gocloud.domain.DatabaseMigration;
import com.cloud7works.gocloud.repository.DatabaseMigrationRepository;
import com.cloud7works.gocloud.repository.search.DatabaseMigrationSearchRepository;
import com.cloud7works.gocloud.service.DatabaseMigrationService;
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

/**
 * Integration tests for the {@Link DatabaseMigrationResource} REST controller.
 */
@SpringBootTest(classes = GoCloudJApp.class)
public class DatabaseMigrationResourceIT {

    private static final String DEFAULT_DB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DB_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DB_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DB_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DB_PASS_WORD = "AAAAAAAAAA";
    private static final String UPDATED_DB_PASS_WORD = "BBBBBBBBBB";

    @Autowired
    private DatabaseMigrationRepository databaseMigrationRepository;

    @Autowired
    private DatabaseMigrationService databaseMigrationService;

    /**
     * This repository is mocked in the com.cloud7works.gocloud.repository.search test package.
     *
     * @see com.cloud7works.gocloud.repository.search.DatabaseMigrationSearchRepositoryMockConfiguration
     */
    @Autowired
    private DatabaseMigrationSearchRepository mockDatabaseMigrationSearchRepository;

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

    private MockMvc restDatabaseMigrationMockMvc;

    private DatabaseMigration databaseMigration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DatabaseMigrationResource databaseMigrationResource = new DatabaseMigrationResource(databaseMigrationService);
        this.restDatabaseMigrationMockMvc = MockMvcBuilders.standaloneSetup(databaseMigrationResource)
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
    public static DatabaseMigration createEntity(EntityManager em) {
        DatabaseMigration databaseMigration = new DatabaseMigration()
            .dbName(DEFAULT_DB_NAME)
            .dbUserName(DEFAULT_DB_USER_NAME)
            .dbPassWord(DEFAULT_DB_PASS_WORD);
        return databaseMigration;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DatabaseMigration createUpdatedEntity(EntityManager em) {
        DatabaseMigration databaseMigration = new DatabaseMigration()
            .dbName(UPDATED_DB_NAME)
            .dbUserName(UPDATED_DB_USER_NAME)
            .dbPassWord(UPDATED_DB_PASS_WORD);
        return databaseMigration;
    }

    @BeforeEach
    public void initTest() {
        databaseMigration = createEntity(em);
    }

    @Test
    @Transactional
    public void createDatabaseMigration() throws Exception {
        int databaseSizeBeforeCreate = databaseMigrationRepository.findAll().size();

        // Create the DatabaseMigration
        restDatabaseMigrationMockMvc.perform(post("/api/database-migrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(databaseMigration)))
            .andExpect(status().isCreated());

        // Validate the DatabaseMigration in the database
        List<DatabaseMigration> databaseMigrationList = databaseMigrationRepository.findAll();
        assertThat(databaseMigrationList).hasSize(databaseSizeBeforeCreate + 1);
        DatabaseMigration testDatabaseMigration = databaseMigrationList.get(databaseMigrationList.size() - 1);
        assertThat(testDatabaseMigration.getDbName()).isEqualTo(DEFAULT_DB_NAME);
        assertThat(testDatabaseMigration.getDbUserName()).isEqualTo(DEFAULT_DB_USER_NAME);
        assertThat(testDatabaseMigration.getDbPassWord()).isEqualTo(DEFAULT_DB_PASS_WORD);

        // Validate the DatabaseMigration in Elasticsearch
        verify(mockDatabaseMigrationSearchRepository, times(1)).save(testDatabaseMigration);
    }

    @Test
    @Transactional
    public void createDatabaseMigrationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = databaseMigrationRepository.findAll().size();

        // Create the DatabaseMigration with an existing ID
        databaseMigration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDatabaseMigrationMockMvc.perform(post("/api/database-migrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(databaseMigration)))
            .andExpect(status().isBadRequest());

        // Validate the DatabaseMigration in the database
        List<DatabaseMigration> databaseMigrationList = databaseMigrationRepository.findAll();
        assertThat(databaseMigrationList).hasSize(databaseSizeBeforeCreate);

        // Validate the DatabaseMigration in Elasticsearch
        verify(mockDatabaseMigrationSearchRepository, times(0)).save(databaseMigration);
    }


    @Test
    @Transactional
    public void getAllDatabaseMigrations() throws Exception {
        // Initialize the database
        databaseMigrationRepository.saveAndFlush(databaseMigration);

        // Get all the databaseMigrationList
        restDatabaseMigrationMockMvc.perform(get("/api/database-migrations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(databaseMigration.getId().intValue())))
            .andExpect(jsonPath("$.[*].dbName").value(hasItem(DEFAULT_DB_NAME.toString())))
            .andExpect(jsonPath("$.[*].dbUserName").value(hasItem(DEFAULT_DB_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].dbPassWord").value(hasItem(DEFAULT_DB_PASS_WORD.toString())));
    }
    
    @Test
    @Transactional
    public void getDatabaseMigration() throws Exception {
        // Initialize the database
        databaseMigrationRepository.saveAndFlush(databaseMigration);

        // Get the databaseMigration
        restDatabaseMigrationMockMvc.perform(get("/api/database-migrations/{id}", databaseMigration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(databaseMigration.getId().intValue()))
            .andExpect(jsonPath("$.dbName").value(DEFAULT_DB_NAME.toString()))
            .andExpect(jsonPath("$.dbUserName").value(DEFAULT_DB_USER_NAME.toString()))
            .andExpect(jsonPath("$.dbPassWord").value(DEFAULT_DB_PASS_WORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDatabaseMigration() throws Exception {
        // Get the databaseMigration
        restDatabaseMigrationMockMvc.perform(get("/api/database-migrations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDatabaseMigration() throws Exception {
        // Initialize the database
        databaseMigrationService.save(databaseMigration);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDatabaseMigrationSearchRepository);

        int databaseSizeBeforeUpdate = databaseMigrationRepository.findAll().size();

        // Update the databaseMigration
        DatabaseMigration updatedDatabaseMigration = databaseMigrationRepository.findById(databaseMigration.getId()).get();
        // Disconnect from session so that the updates on updatedDatabaseMigration are not directly saved in db
        em.detach(updatedDatabaseMigration);
        updatedDatabaseMigration
            .dbName(UPDATED_DB_NAME)
            .dbUserName(UPDATED_DB_USER_NAME)
            .dbPassWord(UPDATED_DB_PASS_WORD);

        restDatabaseMigrationMockMvc.perform(put("/api/database-migrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDatabaseMigration)))
            .andExpect(status().isOk());

        // Validate the DatabaseMigration in the database
        List<DatabaseMigration> databaseMigrationList = databaseMigrationRepository.findAll();
        assertThat(databaseMigrationList).hasSize(databaseSizeBeforeUpdate);
        DatabaseMigration testDatabaseMigration = databaseMigrationList.get(databaseMigrationList.size() - 1);
        assertThat(testDatabaseMigration.getDbName()).isEqualTo(UPDATED_DB_NAME);
        assertThat(testDatabaseMigration.getDbUserName()).isEqualTo(UPDATED_DB_USER_NAME);
        assertThat(testDatabaseMigration.getDbPassWord()).isEqualTo(UPDATED_DB_PASS_WORD);

        // Validate the DatabaseMigration in Elasticsearch
        verify(mockDatabaseMigrationSearchRepository, times(1)).save(testDatabaseMigration);
    }

    @Test
    @Transactional
    public void updateNonExistingDatabaseMigration() throws Exception {
        int databaseSizeBeforeUpdate = databaseMigrationRepository.findAll().size();

        // Create the DatabaseMigration

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDatabaseMigrationMockMvc.perform(put("/api/database-migrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(databaseMigration)))
            .andExpect(status().isBadRequest());

        // Validate the DatabaseMigration in the database
        List<DatabaseMigration> databaseMigrationList = databaseMigrationRepository.findAll();
        assertThat(databaseMigrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DatabaseMigration in Elasticsearch
        verify(mockDatabaseMigrationSearchRepository, times(0)).save(databaseMigration);
    }

    @Test
    @Transactional
    public void deleteDatabaseMigration() throws Exception {
        // Initialize the database
        databaseMigrationService.save(databaseMigration);

        int databaseSizeBeforeDelete = databaseMigrationRepository.findAll().size();

        // Delete the databaseMigration
        restDatabaseMigrationMockMvc.perform(delete("/api/database-migrations/{id}", databaseMigration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<DatabaseMigration> databaseMigrationList = databaseMigrationRepository.findAll();
        assertThat(databaseMigrationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DatabaseMigration in Elasticsearch
        verify(mockDatabaseMigrationSearchRepository, times(1)).deleteById(databaseMigration.getId());
    }

    @Test
    @Transactional
    public void searchDatabaseMigration() throws Exception {
        // Initialize the database
        databaseMigrationService.save(databaseMigration);
        when(mockDatabaseMigrationSearchRepository.search(queryStringQuery("id:" + databaseMigration.getId())))
            .thenReturn(Collections.singletonList(databaseMigration));
        // Search the databaseMigration
        restDatabaseMigrationMockMvc.perform(get("/api/_search/database-migrations?query=id:" + databaseMigration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(databaseMigration.getId().intValue())))
            .andExpect(jsonPath("$.[*].dbName").value(hasItem(DEFAULT_DB_NAME)))
            .andExpect(jsonPath("$.[*].dbUserName").value(hasItem(DEFAULT_DB_USER_NAME)))
            .andExpect(jsonPath("$.[*].dbPassWord").value(hasItem(DEFAULT_DB_PASS_WORD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DatabaseMigration.class);
        DatabaseMigration databaseMigration1 = new DatabaseMigration();
        databaseMigration1.setId(1L);
        DatabaseMigration databaseMigration2 = new DatabaseMigration();
        databaseMigration2.setId(databaseMigration1.getId());
        assertThat(databaseMigration1).isEqualTo(databaseMigration2);
        databaseMigration2.setId(2L);
        assertThat(databaseMigration1).isNotEqualTo(databaseMigration2);
        databaseMigration1.setId(null);
        assertThat(databaseMigration1).isNotEqualTo(databaseMigration2);
    }
}
