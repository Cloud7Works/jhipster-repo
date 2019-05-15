package com.cloud7works.gocloud.service;

import com.cloud7works.gocloud.domain.CloudMigration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link CloudMigration}.
 */
public interface CloudMigrationService {

    /**
     * Save a cloudMigration.
     *
     * @param cloudMigration the entity to save.
     * @return the persisted entity.
     */
    CloudMigration save(CloudMigration cloudMigration);

    /**
     * Get all the cloudMigrations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CloudMigration> findAll(Pageable pageable);


    /**
     * Get the "id" cloudMigration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CloudMigration> findOne(Long id);

    /**
     * Delete the "id" cloudMigration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cloudMigration corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CloudMigration> search(String query, Pageable pageable);
}
