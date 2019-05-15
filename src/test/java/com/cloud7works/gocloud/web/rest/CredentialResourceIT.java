package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.GoCloudJApp;
import com.cloud7works.gocloud.domain.Credential;
import com.cloud7works.gocloud.repository.CredentialRepository;
import com.cloud7works.gocloud.repository.search.CredentialSearchRepository;
import com.cloud7works.gocloud.service.CredentialService;
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
 * Integration tests for the {@Link CredentialResource} REST controller.
 */
@SpringBootTest(classes = GoCloudJApp.class)
public class CredentialResourceIT {

    private static final String DEFAULT_ACCOUNT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CLOUD_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_CLOUD_PROVIDER = "BBBBBBBBBB";

    private static final String DEFAULT_SECRET_KEY = "AAAAAAAAAA";
    private static final String UPDATED_SECRET_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESS_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private CredentialService credentialService;

    /**
     * This repository is mocked in the com.cloud7works.gocloud.repository.search test package.
     *
     * @see com.cloud7works.gocloud.repository.search.CredentialSearchRepositoryMockConfiguration
     */
    @Autowired
    private CredentialSearchRepository mockCredentialSearchRepository;

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

    private MockMvc restCredentialMockMvc;

    private Credential credential;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CredentialResource credentialResource = new CredentialResource(credentialService);
        this.restCredentialMockMvc = MockMvcBuilders.standaloneSetup(credentialResource)
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
    public static Credential createEntity(EntityManager em) {
        Credential credential = new Credential()
            .accountID(DEFAULT_ACCOUNT_ID)
            .cloudProvider(DEFAULT_CLOUD_PROVIDER)
            .secretKey(DEFAULT_SECRET_KEY)
            .accessKey(DEFAULT_ACCESS_KEY)
            .region(DEFAULT_REGION)
            .userId(DEFAULT_USER_ID);
        return credential;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Credential createUpdatedEntity(EntityManager em) {
        Credential credential = new Credential()
            .accountID(UPDATED_ACCOUNT_ID)
            .cloudProvider(UPDATED_CLOUD_PROVIDER)
            .secretKey(UPDATED_SECRET_KEY)
            .accessKey(UPDATED_ACCESS_KEY)
            .region(UPDATED_REGION)
            .userId(UPDATED_USER_ID);
        return credential;
    }

    @BeforeEach
    public void initTest() {
        credential = createEntity(em);
    }

    @Test
    @Transactional
    public void createCredential() throws Exception {
        int databaseSizeBeforeCreate = credentialRepository.findAll().size();

        // Create the Credential
        restCredentialMockMvc.perform(post("/api/credentials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(credential)))
            .andExpect(status().isCreated());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeCreate + 1);
        Credential testCredential = credentialList.get(credentialList.size() - 1);
        assertThat(testCredential.getAccountID()).isEqualTo(DEFAULT_ACCOUNT_ID);
        assertThat(testCredential.getCloudProvider()).isEqualTo(DEFAULT_CLOUD_PROVIDER);
        assertThat(testCredential.getSecretKey()).isEqualTo(DEFAULT_SECRET_KEY);
        assertThat(testCredential.getAccessKey()).isEqualTo(DEFAULT_ACCESS_KEY);
        assertThat(testCredential.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testCredential.getUserId()).isEqualTo(DEFAULT_USER_ID);

        // Validate the Credential in Elasticsearch
        verify(mockCredentialSearchRepository, times(1)).save(testCredential);
    }

    @Test
    @Transactional
    public void createCredentialWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = credentialRepository.findAll().size();

        // Create the Credential with an existing ID
        credential.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCredentialMockMvc.perform(post("/api/credentials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(credential)))
            .andExpect(status().isBadRequest());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeCreate);

        // Validate the Credential in Elasticsearch
        verify(mockCredentialSearchRepository, times(0)).save(credential);
    }


    @Test
    @Transactional
    public void getAllCredentials() throws Exception {
        // Initialize the database
        credentialRepository.saveAndFlush(credential);

        // Get all the credentialList
        restCredentialMockMvc.perform(get("/api/credentials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(credential.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountID").value(hasItem(DEFAULT_ACCOUNT_ID.toString())))
            .andExpect(jsonPath("$.[*].cloudProvider").value(hasItem(DEFAULT_CLOUD_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].secretKey").value(hasItem(DEFAULT_SECRET_KEY.toString())))
            .andExpect(jsonPath("$.[*].accessKey").value(hasItem(DEFAULT_ACCESS_KEY.toString())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getCredential() throws Exception {
        // Initialize the database
        credentialRepository.saveAndFlush(credential);

        // Get the credential
        restCredentialMockMvc.perform(get("/api/credentials/{id}", credential.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(credential.getId().intValue()))
            .andExpect(jsonPath("$.accountID").value(DEFAULT_ACCOUNT_ID.toString()))
            .andExpect(jsonPath("$.cloudProvider").value(DEFAULT_CLOUD_PROVIDER.toString()))
            .andExpect(jsonPath("$.secretKey").value(DEFAULT_SECRET_KEY.toString()))
            .andExpect(jsonPath("$.accessKey").value(DEFAULT_ACCESS_KEY.toString()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCredential() throws Exception {
        // Get the credential
        restCredentialMockMvc.perform(get("/api/credentials/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCredential() throws Exception {
        // Initialize the database
        credentialService.save(credential);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCredentialSearchRepository);

        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();

        // Update the credential
        Credential updatedCredential = credentialRepository.findById(credential.getId()).get();
        // Disconnect from session so that the updates on updatedCredential are not directly saved in db
        em.detach(updatedCredential);
        updatedCredential
            .accountID(UPDATED_ACCOUNT_ID)
            .cloudProvider(UPDATED_CLOUD_PROVIDER)
            .secretKey(UPDATED_SECRET_KEY)
            .accessKey(UPDATED_ACCESS_KEY)
            .region(UPDATED_REGION)
            .userId(UPDATED_USER_ID);

        restCredentialMockMvc.perform(put("/api/credentials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCredential)))
            .andExpect(status().isOk());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);
        Credential testCredential = credentialList.get(credentialList.size() - 1);
        assertThat(testCredential.getAccountID()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testCredential.getCloudProvider()).isEqualTo(UPDATED_CLOUD_PROVIDER);
        assertThat(testCredential.getSecretKey()).isEqualTo(UPDATED_SECRET_KEY);
        assertThat(testCredential.getAccessKey()).isEqualTo(UPDATED_ACCESS_KEY);
        assertThat(testCredential.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testCredential.getUserId()).isEqualTo(UPDATED_USER_ID);

        // Validate the Credential in Elasticsearch
        verify(mockCredentialSearchRepository, times(1)).save(testCredential);
    }

    @Test
    @Transactional
    public void updateNonExistingCredential() throws Exception {
        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();

        // Create the Credential

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCredentialMockMvc.perform(put("/api/credentials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(credential)))
            .andExpect(status().isBadRequest());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Credential in Elasticsearch
        verify(mockCredentialSearchRepository, times(0)).save(credential);
    }

    @Test
    @Transactional
    public void deleteCredential() throws Exception {
        // Initialize the database
        credentialService.save(credential);

        int databaseSizeBeforeDelete = credentialRepository.findAll().size();

        // Delete the credential
        restCredentialMockMvc.perform(delete("/api/credentials/{id}", credential.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Credential in Elasticsearch
        verify(mockCredentialSearchRepository, times(1)).deleteById(credential.getId());
    }

    @Test
    @Transactional
    public void searchCredential() throws Exception {
        // Initialize the database
        credentialService.save(credential);
        when(mockCredentialSearchRepository.search(queryStringQuery("id:" + credential.getId())))
            .thenReturn(Collections.singletonList(credential));
        // Search the credential
        restCredentialMockMvc.perform(get("/api/_search/credentials?query=id:" + credential.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(credential.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountID").value(hasItem(DEFAULT_ACCOUNT_ID)))
            .andExpect(jsonPath("$.[*].cloudProvider").value(hasItem(DEFAULT_CLOUD_PROVIDER)))
            .andExpect(jsonPath("$.[*].secretKey").value(hasItem(DEFAULT_SECRET_KEY)))
            .andExpect(jsonPath("$.[*].accessKey").value(hasItem(DEFAULT_ACCESS_KEY)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Credential.class);
        Credential credential1 = new Credential();
        credential1.setId(1L);
        Credential credential2 = new Credential();
        credential2.setId(credential1.getId());
        assertThat(credential1).isEqualTo(credential2);
        credential2.setId(2L);
        assertThat(credential1).isNotEqualTo(credential2);
        credential1.setId(null);
        assertThat(credential1).isNotEqualTo(credential2);
    }
}
