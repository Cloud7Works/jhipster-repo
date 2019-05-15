package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.GoCloudJApp;
import com.cloud7works.gocloud.domain.WebAppMigration;
import com.cloud7works.gocloud.repository.WebAppMigrationRepository;
import com.cloud7works.gocloud.repository.search.WebAppMigrationSearchRepository;
import com.cloud7works.gocloud.service.WebAppMigrationService;
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
 * Integration tests for the {@Link WebAppMigrationResource} REST controller.
 */
@SpringBootTest(classes = GoCloudJApp.class)
public class WebAppMigrationResourceIT {

    @Autowired
    private WebAppMigrationRepository webAppMigrationRepository;

    @Autowired
    private WebAppMigrationService webAppMigrationService;

    /**
     * This repository is mocked in the com.cloud7works.gocloud.repository.search test package.
     *
     * @see com.cloud7works.gocloud.repository.search.WebAppMigrationSearchRepositoryMockConfiguration
     */
    @Autowired
    private WebAppMigrationSearchRepository mockWebAppMigrationSearchRepository;

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

    private MockMvc restWebAppMigrationMockMvc;

    private WebAppMigration webAppMigration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WebAppMigrationResource webAppMigrationResource = new WebAppMigrationResource(webAppMigrationService);
        this.restWebAppMigrationMockMvc = MockMvcBuilders.standaloneSetup(webAppMigrationResource)
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
    public static WebAppMigration createEntity(EntityManager em) {
        WebAppMigration webAppMigration = new WebAppMigration();
        return webAppMigration;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebAppMigration createUpdatedEntity(EntityManager em) {
        WebAppMigration webAppMigration = new WebAppMigration();
        return webAppMigration;
    }

    @BeforeEach
    public void initTest() {
        webAppMigration = createEntity(em);
    }

    @Test
    @Transactional
    public void createWebAppMigration() throws Exception {
        int databaseSizeBeforeCreate = webAppMigrationRepository.findAll().size();

        // Create the WebAppMigration
        restWebAppMigrationMockMvc.perform(post("/api/web-app-migrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(webAppMigration)))
            .andExpect(status().isCreated());

        // Validate the WebAppMigration in the database
        List<WebAppMigration> webAppMigrationList = webAppMigrationRepository.findAll();
        assertThat(webAppMigrationList).hasSize(databaseSizeBeforeCreate + 1);
        WebAppMigration testWebAppMigration = webAppMigrationList.get(webAppMigrationList.size() - 1);

        // Validate the WebAppMigration in Elasticsearch
        verify(mockWebAppMigrationSearchRepository, times(1)).save(testWebAppMigration);
    }

    @Test
    @Transactional
    public void createWebAppMigrationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = webAppMigrationRepository.findAll().size();

        // Create the WebAppMigration with an existing ID
        webAppMigration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWebAppMigrationMockMvc.perform(post("/api/web-app-migrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(webAppMigration)))
            .andExpect(status().isBadRequest());

        // Validate the WebAppMigration in the database
        List<WebAppMigration> webAppMigrationList = webAppMigrationRepository.findAll();
        assertThat(webAppMigrationList).hasSize(databaseSizeBeforeCreate);

        // Validate the WebAppMigration in Elasticsearch
        verify(mockWebAppMigrationSearchRepository, times(0)).save(webAppMigration);
    }


    @Test
    @Transactional
    public void getAllWebAppMigrations() throws Exception {
        // Initialize the database
        webAppMigrationRepository.saveAndFlush(webAppMigration);

        // Get all the webAppMigrationList
        restWebAppMigrationMockMvc.perform(get("/api/web-app-migrations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webAppMigration.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getWebAppMigration() throws Exception {
        // Initialize the database
        webAppMigrationRepository.saveAndFlush(webAppMigration);

        // Get the webAppMigration
        restWebAppMigrationMockMvc.perform(get("/api/web-app-migrations/{id}", webAppMigration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(webAppMigration.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWebAppMigration() throws Exception {
        // Get the webAppMigration
        restWebAppMigrationMockMvc.perform(get("/api/web-app-migrations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWebAppMigration() throws Exception {
        // Initialize the database
        webAppMigrationService.save(webAppMigration);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockWebAppMigrationSearchRepository);

        int databaseSizeBeforeUpdate = webAppMigrationRepository.findAll().size();

        // Update the webAppMigration
        WebAppMigration updatedWebAppMigration = webAppMigrationRepository.findById(webAppMigration.getId()).get();
        // Disconnect from session so that the updates on updatedWebAppMigration are not directly saved in db
        em.detach(updatedWebAppMigration);

        restWebAppMigrationMockMvc.perform(put("/api/web-app-migrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWebAppMigration)))
            .andExpect(status().isOk());

        // Validate the WebAppMigration in the database
        List<WebAppMigration> webAppMigrationList = webAppMigrationRepository.findAll();
        assertThat(webAppMigrationList).hasSize(databaseSizeBeforeUpdate);
        WebAppMigration testWebAppMigration = webAppMigrationList.get(webAppMigrationList.size() - 1);

        // Validate the WebAppMigration in Elasticsearch
        verify(mockWebAppMigrationSearchRepository, times(1)).save(testWebAppMigration);
    }

    @Test
    @Transactional
    public void updateNonExistingWebAppMigration() throws Exception {
        int databaseSizeBeforeUpdate = webAppMigrationRepository.findAll().size();

        // Create the WebAppMigration

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebAppMigrationMockMvc.perform(put("/api/web-app-migrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(webAppMigration)))
            .andExpect(status().isBadRequest());

        // Validate the WebAppMigration in the database
        List<WebAppMigration> webAppMigrationList = webAppMigrationRepository.findAll();
        assertThat(webAppMigrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WebAppMigration in Elasticsearch
        verify(mockWebAppMigrationSearchRepository, times(0)).save(webAppMigration);
    }

    @Test
    @Transactional
    public void deleteWebAppMigration() throws Exception {
        // Initialize the database
        webAppMigrationService.save(webAppMigration);

        int databaseSizeBeforeDelete = webAppMigrationRepository.findAll().size();

        // Delete the webAppMigration
        restWebAppMigrationMockMvc.perform(delete("/api/web-app-migrations/{id}", webAppMigration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<WebAppMigration> webAppMigrationList = webAppMigrationRepository.findAll();
        assertThat(webAppMigrationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WebAppMigration in Elasticsearch
        verify(mockWebAppMigrationSearchRepository, times(1)).deleteById(webAppMigration.getId());
    }

    @Test
    @Transactional
    public void searchWebAppMigration() throws Exception {
        // Initialize the database
        webAppMigrationService.save(webAppMigration);
        when(mockWebAppMigrationSearchRepository.search(queryStringQuery("id:" + webAppMigration.getId())))
            .thenReturn(Collections.singletonList(webAppMigration));
        // Search the webAppMigration
        restWebAppMigrationMockMvc.perform(get("/api/_search/web-app-migrations?query=id:" + webAppMigration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webAppMigration.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebAppMigration.class);
        WebAppMigration webAppMigration1 = new WebAppMigration();
        webAppMigration1.setId(1L);
        WebAppMigration webAppMigration2 = new WebAppMigration();
        webAppMigration2.setId(webAppMigration1.getId());
        assertThat(webAppMigration1).isEqualTo(webAppMigration2);
        webAppMigration2.setId(2L);
        assertThat(webAppMigration1).isNotEqualTo(webAppMigration2);
        webAppMigration1.setId(null);
        assertThat(webAppMigration1).isNotEqualTo(webAppMigration2);
    }
}
