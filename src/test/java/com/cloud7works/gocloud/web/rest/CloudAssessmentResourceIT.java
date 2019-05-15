package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.GoCloudJApp;
import com.cloud7works.gocloud.domain.CloudAssessment;
import com.cloud7works.gocloud.repository.CloudAssessmentRepository;
import com.cloud7works.gocloud.repository.search.CloudAssessmentSearchRepository;
import com.cloud7works.gocloud.service.CloudAssessmentService;
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
 * Integration tests for the {@Link CloudAssessmentResource} REST controller.
 */
@SpringBootTest(classes = GoCloudJApp.class)
public class CloudAssessmentResourceIT {

    private static final String DEFAULT_ASSESSMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSESSMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_CHOICE = "AAAAAAAAAA";
    private static final String UPDATED_CHOICE = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION_OBJECTIVE = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_OBJECTIVE = "BBBBBBBBBB";

    private static final String DEFAULT_USER_SELECTION = "AAAAAAAAAA";
    private static final String UPDATED_USER_SELECTION = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    @Autowired
    private CloudAssessmentRepository cloudAssessmentRepository;

    @Autowired
    private CloudAssessmentService cloudAssessmentService;

    /**
     * This repository is mocked in the com.cloud7works.gocloud.repository.search test package.
     *
     * @see com.cloud7works.gocloud.repository.search.CloudAssessmentSearchRepositoryMockConfiguration
     */
    @Autowired
    private CloudAssessmentSearchRepository mockCloudAssessmentSearchRepository;

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

    private MockMvc restCloudAssessmentMockMvc;

    private CloudAssessment cloudAssessment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CloudAssessmentResource cloudAssessmentResource = new CloudAssessmentResource(cloudAssessmentService);
        this.restCloudAssessmentMockMvc = MockMvcBuilders.standaloneSetup(cloudAssessmentResource)
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
    public static CloudAssessment createEntity(EntityManager em) {
        CloudAssessment cloudAssessment = new CloudAssessment()
            .assessmentId(DEFAULT_ASSESSMENT_ID)
            .questionId(DEFAULT_QUESTION_ID)
            .question(DEFAULT_QUESTION)
            .choice(DEFAULT_CHOICE)
            .questionObjective(DEFAULT_QUESTION_OBJECTIVE)
            .userSelection(DEFAULT_USER_SELECTION)
            .userId(DEFAULT_USER_ID);
        return cloudAssessment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CloudAssessment createUpdatedEntity(EntityManager em) {
        CloudAssessment cloudAssessment = new CloudAssessment()
            .assessmentId(UPDATED_ASSESSMENT_ID)
            .questionId(UPDATED_QUESTION_ID)
            .question(UPDATED_QUESTION)
            .choice(UPDATED_CHOICE)
            .questionObjective(UPDATED_QUESTION_OBJECTIVE)
            .userSelection(UPDATED_USER_SELECTION)
            .userId(UPDATED_USER_ID);
        return cloudAssessment;
    }

    @BeforeEach
    public void initTest() {
        cloudAssessment = createEntity(em);
    }

    @Test
    @Transactional
    public void createCloudAssessment() throws Exception {
        int databaseSizeBeforeCreate = cloudAssessmentRepository.findAll().size();

        // Create the CloudAssessment
        restCloudAssessmentMockMvc.perform(post("/api/cloud-assessments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cloudAssessment)))
            .andExpect(status().isCreated());

        // Validate the CloudAssessment in the database
        List<CloudAssessment> cloudAssessmentList = cloudAssessmentRepository.findAll();
        assertThat(cloudAssessmentList).hasSize(databaseSizeBeforeCreate + 1);
        CloudAssessment testCloudAssessment = cloudAssessmentList.get(cloudAssessmentList.size() - 1);
        assertThat(testCloudAssessment.getAssessmentId()).isEqualTo(DEFAULT_ASSESSMENT_ID);
        assertThat(testCloudAssessment.getQuestionId()).isEqualTo(DEFAULT_QUESTION_ID);
        assertThat(testCloudAssessment.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testCloudAssessment.getChoice()).isEqualTo(DEFAULT_CHOICE);
        assertThat(testCloudAssessment.getQuestionObjective()).isEqualTo(DEFAULT_QUESTION_OBJECTIVE);
        assertThat(testCloudAssessment.getUserSelection()).isEqualTo(DEFAULT_USER_SELECTION);
        assertThat(testCloudAssessment.getUserId()).isEqualTo(DEFAULT_USER_ID);

        // Validate the CloudAssessment in Elasticsearch
        verify(mockCloudAssessmentSearchRepository, times(1)).save(testCloudAssessment);
    }

    @Test
    @Transactional
    public void createCloudAssessmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cloudAssessmentRepository.findAll().size();

        // Create the CloudAssessment with an existing ID
        cloudAssessment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCloudAssessmentMockMvc.perform(post("/api/cloud-assessments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cloudAssessment)))
            .andExpect(status().isBadRequest());

        // Validate the CloudAssessment in the database
        List<CloudAssessment> cloudAssessmentList = cloudAssessmentRepository.findAll();
        assertThat(cloudAssessmentList).hasSize(databaseSizeBeforeCreate);

        // Validate the CloudAssessment in Elasticsearch
        verify(mockCloudAssessmentSearchRepository, times(0)).save(cloudAssessment);
    }


    @Test
    @Transactional
    public void getAllCloudAssessments() throws Exception {
        // Initialize the database
        cloudAssessmentRepository.saveAndFlush(cloudAssessment);

        // Get all the cloudAssessmentList
        restCloudAssessmentMockMvc.perform(get("/api/cloud-assessments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cloudAssessment.getId().intValue())))
            .andExpect(jsonPath("$.[*].assessmentId").value(hasItem(DEFAULT_ASSESSMENT_ID.toString())))
            .andExpect(jsonPath("$.[*].questionId").value(hasItem(DEFAULT_QUESTION_ID.toString())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())))
            .andExpect(jsonPath("$.[*].choice").value(hasItem(DEFAULT_CHOICE.toString())))
            .andExpect(jsonPath("$.[*].questionObjective").value(hasItem(DEFAULT_QUESTION_OBJECTIVE.toString())))
            .andExpect(jsonPath("$.[*].userSelection").value(hasItem(DEFAULT_USER_SELECTION.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getCloudAssessment() throws Exception {
        // Initialize the database
        cloudAssessmentRepository.saveAndFlush(cloudAssessment);

        // Get the cloudAssessment
        restCloudAssessmentMockMvc.perform(get("/api/cloud-assessments/{id}", cloudAssessment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cloudAssessment.getId().intValue()))
            .andExpect(jsonPath("$.assessmentId").value(DEFAULT_ASSESSMENT_ID.toString()))
            .andExpect(jsonPath("$.questionId").value(DEFAULT_QUESTION_ID.toString()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION.toString()))
            .andExpect(jsonPath("$.choice").value(DEFAULT_CHOICE.toString()))
            .andExpect(jsonPath("$.questionObjective").value(DEFAULT_QUESTION_OBJECTIVE.toString()))
            .andExpect(jsonPath("$.userSelection").value(DEFAULT_USER_SELECTION.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCloudAssessment() throws Exception {
        // Get the cloudAssessment
        restCloudAssessmentMockMvc.perform(get("/api/cloud-assessments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCloudAssessment() throws Exception {
        // Initialize the database
        cloudAssessmentService.save(cloudAssessment);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCloudAssessmentSearchRepository);

        int databaseSizeBeforeUpdate = cloudAssessmentRepository.findAll().size();

        // Update the cloudAssessment
        CloudAssessment updatedCloudAssessment = cloudAssessmentRepository.findById(cloudAssessment.getId()).get();
        // Disconnect from session so that the updates on updatedCloudAssessment are not directly saved in db
        em.detach(updatedCloudAssessment);
        updatedCloudAssessment
            .assessmentId(UPDATED_ASSESSMENT_ID)
            .questionId(UPDATED_QUESTION_ID)
            .question(UPDATED_QUESTION)
            .choice(UPDATED_CHOICE)
            .questionObjective(UPDATED_QUESTION_OBJECTIVE)
            .userSelection(UPDATED_USER_SELECTION)
            .userId(UPDATED_USER_ID);

        restCloudAssessmentMockMvc.perform(put("/api/cloud-assessments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCloudAssessment)))
            .andExpect(status().isOk());

        // Validate the CloudAssessment in the database
        List<CloudAssessment> cloudAssessmentList = cloudAssessmentRepository.findAll();
        assertThat(cloudAssessmentList).hasSize(databaseSizeBeforeUpdate);
        CloudAssessment testCloudAssessment = cloudAssessmentList.get(cloudAssessmentList.size() - 1);
        assertThat(testCloudAssessment.getAssessmentId()).isEqualTo(UPDATED_ASSESSMENT_ID);
        assertThat(testCloudAssessment.getQuestionId()).isEqualTo(UPDATED_QUESTION_ID);
        assertThat(testCloudAssessment.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testCloudAssessment.getChoice()).isEqualTo(UPDATED_CHOICE);
        assertThat(testCloudAssessment.getQuestionObjective()).isEqualTo(UPDATED_QUESTION_OBJECTIVE);
        assertThat(testCloudAssessment.getUserSelection()).isEqualTo(UPDATED_USER_SELECTION);
        assertThat(testCloudAssessment.getUserId()).isEqualTo(UPDATED_USER_ID);

        // Validate the CloudAssessment in Elasticsearch
        verify(mockCloudAssessmentSearchRepository, times(1)).save(testCloudAssessment);
    }

    @Test
    @Transactional
    public void updateNonExistingCloudAssessment() throws Exception {
        int databaseSizeBeforeUpdate = cloudAssessmentRepository.findAll().size();

        // Create the CloudAssessment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCloudAssessmentMockMvc.perform(put("/api/cloud-assessments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cloudAssessment)))
            .andExpect(status().isBadRequest());

        // Validate the CloudAssessment in the database
        List<CloudAssessment> cloudAssessmentList = cloudAssessmentRepository.findAll();
        assertThat(cloudAssessmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CloudAssessment in Elasticsearch
        verify(mockCloudAssessmentSearchRepository, times(0)).save(cloudAssessment);
    }

    @Test
    @Transactional
    public void deleteCloudAssessment() throws Exception {
        // Initialize the database
        cloudAssessmentService.save(cloudAssessment);

        int databaseSizeBeforeDelete = cloudAssessmentRepository.findAll().size();

        // Delete the cloudAssessment
        restCloudAssessmentMockMvc.perform(delete("/api/cloud-assessments/{id}", cloudAssessment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<CloudAssessment> cloudAssessmentList = cloudAssessmentRepository.findAll();
        assertThat(cloudAssessmentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CloudAssessment in Elasticsearch
        verify(mockCloudAssessmentSearchRepository, times(1)).deleteById(cloudAssessment.getId());
    }

    @Test
    @Transactional
    public void searchCloudAssessment() throws Exception {
        // Initialize the database
        cloudAssessmentService.save(cloudAssessment);
        when(mockCloudAssessmentSearchRepository.search(queryStringQuery("id:" + cloudAssessment.getId())))
            .thenReturn(Collections.singletonList(cloudAssessment));
        // Search the cloudAssessment
        restCloudAssessmentMockMvc.perform(get("/api/_search/cloud-assessments?query=id:" + cloudAssessment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cloudAssessment.getId().intValue())))
            .andExpect(jsonPath("$.[*].assessmentId").value(hasItem(DEFAULT_ASSESSMENT_ID)))
            .andExpect(jsonPath("$.[*].questionId").value(hasItem(DEFAULT_QUESTION_ID)))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].choice").value(hasItem(DEFAULT_CHOICE)))
            .andExpect(jsonPath("$.[*].questionObjective").value(hasItem(DEFAULT_QUESTION_OBJECTIVE)))
            .andExpect(jsonPath("$.[*].userSelection").value(hasItem(DEFAULT_USER_SELECTION)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CloudAssessment.class);
        CloudAssessment cloudAssessment1 = new CloudAssessment();
        cloudAssessment1.setId(1L);
        CloudAssessment cloudAssessment2 = new CloudAssessment();
        cloudAssessment2.setId(cloudAssessment1.getId());
        assertThat(cloudAssessment1).isEqualTo(cloudAssessment2);
        cloudAssessment2.setId(2L);
        assertThat(cloudAssessment1).isNotEqualTo(cloudAssessment2);
        cloudAssessment1.setId(null);
        assertThat(cloudAssessment1).isNotEqualTo(cloudAssessment2);
    }
}
