package com.cloud7works.gocloud.service;

import com.cloud7works.gocloud.domain.Credential;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Credential}.
 */
public interface CredentialService {

    /**
     * Save a credential.
     *
     * @param credential the entity to save.
     * @return the persisted entity.
     */
    Credential save(Credential credential);

    /**
     * Get all the credentials.
     *
     * @return the list of entities.
     */
    List<Credential> findAll();


    /**
     * Get the "id" credential.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Credential> findOne(Long id);

    /**
     * Delete the "id" credential.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the credential corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<Credential> search(String query);
}
