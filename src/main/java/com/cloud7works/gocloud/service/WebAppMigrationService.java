package com.cloud7works.gocloud.service;

import com.cloud7works.gocloud.domain.WebAppMigration;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link WebAppMigration}.
 */
public interface WebAppMigrationService {

    /**
     * Save a webAppMigration.
     *
     * @param webAppMigration the entity to save.
     * @return the persisted entity.
     */
    WebAppMigration save(WebAppMigration webAppMigration);

    /**
     * Get all the webAppMigrations.
     *
     * @return the list of entities.
     */
    List<WebAppMigration> findAll();


    /**
     * Get the "id" webAppMigration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WebAppMigration> findOne(Long id);

    /**
     * Delete the "id" webAppMigration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the webAppMigration corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<WebAppMigration> search(String query);
}
