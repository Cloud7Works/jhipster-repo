package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.GoCloudJApp;
import com.cloud7works.gocloud.domain.CloudGovernance;
import com.cloud7works.gocloud.repository.CloudGovernanceRepository;
import com.cloud7works.gocloud.repository.search.CloudGovernanceSearchRepository;
import com.cloud7works.gocloud.service.CloudGovernanceService;
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
 * Integration tests for the {@Link CloudGovernanceResource} REST controller.
 */
@SpringBootTest(classes = GoCloudJApp.class)
public class CloudGovernanceResourceIT {

    private static final String DEFAULT_GOVERNANCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_GOVERNANCE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_ACTIVE_APPS = 1;
    private static final Integer UPDATED_ACTIVE_APPS = 2;

    private static final Integer DEFAULT_IN_ACTIVE_APPS = 1;
    private static final Integer UPDATED_IN_ACTIVE_APPS = 2;

    private static final Integer DEFAULT_CURRENT_SPEND_ACROSS = 1;
    private static final Integer UPDATED_CURRENT_SPEND_ACROSS = 2;

    private static final Integer DEFAULT_ESTIMATED_SPEND_ACROSS = 1;
    private static final Integer UPDATED_ESTIMATED_SPEND_ACROSS = 2;

    private static final String DEFAULT_SECURITY_ALERTS_ACROSS = "AAAAAAAAAA";
    private static final String UPDATED_SECURITY_ALERTS_ACROSS = "BBBBBBBBBB";

    @Autowired
    private CloudGovernanceRepository cloudGovernanceRepository;

    @Autowired
    private CloudGovernanceService cloudGovernanceService;

    /**
     * This repository is mocked in the com.cloud7works.gocloud.repository.search test package.
     *
     * @see com.cloud7works.gocloud.repository.search.CloudGovernanceSearchRepositoryMockConfiguration
     */
    @Autowired
    private CloudGovernanceSearchRepository mockCloudGovernanceSearchRepository;

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

    private MockMvc restCloudGovernanceMockMvc;

    private CloudGovernance cloudGovernance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CloudGovernanceResource cloudGovernanceResource = new CloudGovernanceResource(cloudGovernanceService);
        this.restCloudGovernanceMockMvc = MockMvcBuilders.standaloneSetup(cloudGovernanceResource)
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
    public static CloudGovernance createEntity(EntityManager em) {
        CloudGovernance cloudGovernance = new CloudGovernance()
            .governanceId(DEFAULT_GOVERNANCE_ID)
            .userId(DEFAULT_USER_ID)
            .activeApps(DEFAULT_ACTIVE_APPS)
            .inActiveApps(DEFAULT_IN_ACTIVE_APPS)
            .currentSpendAcross(DEFAULT_CURRENT_SPEND_ACROSS)
            .estimatedSpendAcross(DEFAULT_ESTIMATED_SPEND_ACROSS)
            .securityAlertsAcross(DEFAULT_SECURITY_ALERTS_ACROSS);
        return cloudGovernance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CloudGovernance createUpdatedEntity(EntityManager em) {
        CloudGovernance cloudGovernance = new CloudGovernance()
            .governanceId(UPDATED_GOVERNANCE_ID)
            .userId(UPDATED_USER_ID)
            .activeApps(UPDATED_ACTIVE_APPS)
            .inActiveApps(UPDATED_IN_ACTIVE_APPS)
            .currentSpendAcross(UPDATED_CURRENT_SPEND_ACROSS)
            .estimatedSpendAcross(UPDATED_ESTIMATED_SPEND_ACROSS)
            .securityAlertsAcross(UPDATED_SECURITY_ALERTS_ACROSS);
        return cloudGovernance;
    }

    @BeforeEach
    public void initTest() {
        cloudGovernance = createEntity(em);
    }

    @Test
    @Transactional
    public void createCloudGovernance() throws Exception {
        int databaseSizeBeforeCreate = cloudGovernanceRepository.findAll().size();

        // Create the CloudGovernance
        restCloudGovernanceMockMvc.perform(post("/api/cloud-governances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cloudGovernance)))
            .andExpect(status().isCreated());

        // Validate the CloudGovernance in the database
        List<CloudGovernance> cloudGovernanceList = cloudGovernanceRepository.findAll();
        assertThat(cloudGovernanceList).hasSize(databaseSizeBeforeCreate + 1);
        CloudGovernance testCloudGovernance = cloudGovernanceList.get(cloudGovernanceList.size() - 1);
        assertThat(testCloudGovernance.getGovernanceId()).isEqualTo(DEFAULT_GOVERNANCE_ID);
        assertThat(testCloudGovernance.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testCloudGovernance.getActiveApps()).isEqualTo(DEFAULT_ACTIVE_APPS);
        assertThat(testCloudGovernance.getInActiveApps()).isEqualTo(DEFAULT_IN_ACTIVE_APPS);
        assertThat(testCloudGovernance.getCurrentSpendAcross()).isEqualTo(DEFAULT_CURRENT_SPEND_ACROSS);
        assertThat(testCloudGovernance.getEstimatedSpendAcross()).isEqualTo(DEFAULT_ESTIMATED_SPEND_ACROSS);
        assertThat(testCloudGovernance.getSecurityAlertsAcross()).isEqualTo(DEFAULT_SECURITY_ALERTS_ACROSS);

        // Validate the CloudGovernance in Elasticsearch
        verify(mockCloudGovernanceSearchRepository, times(1)).save(testCloudGovernance);
    }

    @Test
    @Transactional
    public void createCloudGovernanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cloudGovernanceRepository.findAll().size();

        // Create the CloudGovernance with an existing ID
        cloudGovernance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCloudGovernanceMockMvc.perform(post("/api/cloud-governances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cloudGovernance)))
            .andExpect(status().isBadRequest());

        // Validate the CloudGovernance in the database
        List<CloudGovernance> cloudGovernanceList = cloudGovernanceRepository.findAll();
        assertThat(cloudGovernanceList).hasSize(databaseSizeBeforeCreate);

        // Validate the CloudGovernance in Elasticsearch
        verify(mockCloudGovernanceSearchRepository, times(0)).save(cloudGovernance);
    }


    @Test
    @Transactional
    public void getAllCloudGovernances() throws Exception {
        // Initialize the database
        cloudGovernanceRepository.saveAndFlush(cloudGovernance);

        // Get all the cloudGovernanceList
        restCloudGovernanceMockMvc.perform(get("/api/cloud-governances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cloudGovernance.getId().intValue())))
            .andExpect(jsonPath("$.[*].governanceId").value(hasItem(DEFAULT_GOVERNANCE_ID.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].activeApps").value(hasItem(DEFAULT_ACTIVE_APPS)))
            .andExpect(jsonPath("$.[*].inActiveApps").value(hasItem(DEFAULT_IN_ACTIVE_APPS)))
            .andExpect(jsonPath("$.[*].currentSpendAcross").value(hasItem(DEFAULT_CURRENT_SPEND_ACROSS)))
            .andExpect(jsonPath("$.[*].estimatedSpendAcross").value(hasItem(DEFAULT_ESTIMATED_SPEND_ACROSS)))
            .andExpect(jsonPath("$.[*].securityAlertsAcross").value(hasItem(DEFAULT_SECURITY_ALERTS_ACROSS.toString())));
    }
    
    @Test
    @Transactional
    public void getCloudGovernance() throws Exception {
        // Initialize the database
        cloudGovernanceRepository.saveAndFlush(cloudGovernance);

        // Get the cloudGovernance
        restCloudGovernanceMockMvc.perform(get("/api/cloud-governances/{id}", cloudGovernance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cloudGovernance.getId().intValue()))
            .andExpect(jsonPath("$.governanceId").value(DEFAULT_GOVERNANCE_ID.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.activeApps").value(DEFAULT_ACTIVE_APPS))
            .andExpect(jsonPath("$.inActiveApps").value(DEFAULT_IN_ACTIVE_APPS))
            .andExpect(jsonPath("$.currentSpendAcross").value(DEFAULT_CURRENT_SPEND_ACROSS))
            .andExpect(jsonPath("$.estimatedSpendAcross").value(DEFAULT_ESTIMATED_SPEND_ACROSS))
            .andExpect(jsonPath("$.securityAlertsAcross").value(DEFAULT_SECURITY_ALERTS_ACROSS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCloudGovernance() throws Exception {
        // Get the cloudGovernance
        restCloudGovernanceMockMvc.perform(get("/api/cloud-governances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCloudGovernance() throws Exception {
        // Initialize the database
        cloudGovernanceService.save(cloudGovernance);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCloudGovernanceSearchRepository);

        int databaseSizeBeforeUpdate = cloudGovernanceRepository.findAll().size();

        // Update the cloudGovernance
        CloudGovernance updatedCloudGovernance = cloudGovernanceRepository.findById(cloudGovernance.getId()).get();
        // Disconnect from session so that the updates on updatedCloudGovernance are not directly saved in db
        em.detach(updatedCloudGovernance);
        updatedCloudGovernance
            .governanceId(UPDATED_GOVERNANCE_ID)
            .userId(UPDATED_USER_ID)
            .activeApps(UPDATED_ACTIVE_APPS)
            .inActiveApps(UPDATED_IN_ACTIVE_APPS)
            .currentSpendAcross(UPDATED_CURRENT_SPEND_ACROSS)
            .estimatedSpendAcross(UPDATED_ESTIMATED_SPEND_ACROSS)
            .securityAlertsAcross(UPDATED_SECURITY_ALERTS_ACROSS);

        restCloudGovernanceMockMvc.perform(put("/api/cloud-governances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCloudGovernance)))
            .andExpect(status().isOk());

        // Validate the CloudGovernance in the database
        List<CloudGovernance> cloudGovernanceList = cloudGovernanceRepository.findAll();
        assertThat(cloudGovernanceList).hasSize(databaseSizeBeforeUpdate);
        CloudGovernance testCloudGovernance = cloudGovernanceList.get(cloudGovernanceList.size() - 1);
        assertThat(testCloudGovernance.getGovernanceId()).isEqualTo(UPDATED_GOVERNANCE_ID);
        assertThat(testCloudGovernance.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testCloudGovernance.getActiveApps()).isEqualTo(UPDATED_ACTIVE_APPS);
        assertThat(testCloudGovernance.getInActiveApps()).isEqualTo(UPDATED_IN_ACTIVE_APPS);
        assertThat(testCloudGovernance.getCurrentSpendAcross()).isEqualTo(UPDATED_CURRENT_SPEND_ACROSS);
        assertThat(testCloudGovernance.getEstimatedSpendAcross()).isEqualTo(UPDATED_ESTIMATED_SPEND_ACROSS);
        assertThat(testCloudGovernance.getSecurityAlertsAcross()).isEqualTo(UPDATED_SECURITY_ALERTS_ACROSS);

        // Validate the CloudGovernance in Elasticsearch
        verify(mockCloudGovernanceSearchRepository, times(1)).save(testCloudGovernance);
    }

    @Test
    @Transactional
    public void updateNonExistingCloudGovernance() throws Exception {
        int databaseSizeBeforeUpdate = cloudGovernanceRepository.findAll().size();

        // Create the CloudGovernance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCloudGovernanceMockMvc.perform(put("/api/cloud-governances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cloudGovernance)))
            .andExpect(status().isBadRequest());

        // Validate the CloudGovernance in the database
        List<CloudGovernance> cloudGovernanceList = cloudGovernanceRepository.findAll();
        assertThat(cloudGovernanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CloudGovernance in Elasticsearch
        verify(mockCloudGovernanceSearchRepository, times(0)).save(cloudGovernance);
    }

    @Test
    @Transactional
    public void deleteCloudGovernance() throws Exception {
        // Initialize the database
        cloudGovernanceService.save(cloudGovernance);

        int databaseSizeBeforeDelete = cloudGovernanceRepository.findAll().size();

        // Delete the cloudGovernance
        restCloudGovernanceMockMvc.perform(delete("/api/cloud-governances/{id}", cloudGovernance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<CloudGovernance> cloudGovernanceList = cloudGovernanceRepository.findAll();
        assertThat(cloudGovernanceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CloudGovernance in Elasticsearch
        verify(mockCloudGovernanceSearchRepository, times(1)).deleteById(cloudGovernance.getId());
    }

    @Test
    @Transactional
    public void searchCloudGovernance() throws Exception {
        // Initialize the database
        cloudGovernanceService.save(cloudGovernance);
        when(mockCloudGovernanceSearchRepository.search(queryStringQuery("id:" + cloudGovernance.getId())))
            .thenReturn(Collections.singletonList(cloudGovernance));
        // Search the cloudGovernance
        restCloudGovernanceMockMvc.perform(get("/api/_search/cloud-governances?query=id:" + cloudGovernance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cloudGovernance.getId().intValue())))
            .andExpect(jsonPath("$.[*].governanceId").value(hasItem(DEFAULT_GOVERNANCE_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].activeApps").value(hasItem(DEFAULT_ACTIVE_APPS)))
            .andExpect(jsonPath("$.[*].inActiveApps").value(hasItem(DEFAULT_IN_ACTIVE_APPS)))
            .andExpect(jsonPath("$.[*].currentSpendAcross").value(hasItem(DEFAULT_CURRENT_SPEND_ACROSS)))
            .andExpect(jsonPath("$.[*].estimatedSpendAcross").value(hasItem(DEFAULT_ESTIMATED_SPEND_ACROSS)))
            .andExpect(jsonPath("$.[*].securityAlertsAcross").value(hasItem(DEFAULT_SECURITY_ALERTS_ACROSS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CloudGovernance.class);
        CloudGovernance cloudGovernance1 = new CloudGovernance();
        cloudGovernance1.setId(1L);
        CloudGovernance cloudGovernance2 = new CloudGovernance();
        cloudGovernance2.setId(cloudGovernance1.getId());
        assertThat(cloudGovernance1).isEqualTo(cloudGovernance2);
        cloudGovernance2.setId(2L);
        assertThat(cloudGovernance1).isNotEqualTo(cloudGovernance2);
        cloudGovernance1.setId(null);
        assertThat(cloudGovernance1).isNotEqualTo(cloudGovernance2);
    }
}
