package com.cloud7works.gocloud.service.impl;

import com.cloud7works.gocloud.service.CredentialService;
import com.cloud7works.gocloud.domain.Credential;
import com.cloud7works.gocloud.repository.CredentialRepository;
import com.cloud7works.gocloud.repository.search.CredentialSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Credential}.
 */
@Service
@Transactional
public class CredentialServiceImpl implements CredentialService {

    private final Logger log = LoggerFactory.getLogger(CredentialServiceImpl.class);

    private final CredentialRepository credentialRepository;

    private final CredentialSearchRepository credentialSearchRepository;

    public CredentialServiceImpl(CredentialRepository credentialRepository, CredentialSearchRepository credentialSearchRepository) {
        this.credentialRepository = credentialRepository;
        this.credentialSearchRepository = credentialSearchRepository;
    }

    /**
     * Save a credential.
     *
     * @param credential the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Credential save(Credential credential) {
        log.debug("Request to save Credential : {}", credential);
        Credential result = credentialRepository.save(credential);
        credentialSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the credentials.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Credential> findAll() {
        log.debug("Request to get all Credentials");
        return credentialRepository.findAll();
    }


    /**
     * Get one credential by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Credential> findOne(Long id) {
        log.debug("Request to get Credential : {}", id);
        return credentialRepository.findById(id);
    }

    /**
     * Delete the credential by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Credential : {}", id);
        credentialRepository.deleteById(id);
        credentialSearchRepository.deleteById(id);
    }

    /**
     * Search for the credential corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Credential> search(String query) {
        log.debug("Request to search Credentials for query {}", query);
        return StreamSupport
            .stream(credentialSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
