package com.cloud7works.gocloud.service;

import com.cloud7works.gocloud.domain.DatabaseMigration;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link DatabaseMigration}.
 */
public interface DatabaseMigrationService {

    /**
     * Save a databaseMigration.
     *
     * @param databaseMigration the entity to save.
     * @return the persisted entity.
     */
    DatabaseMigration save(DatabaseMigration databaseMigration);

    /**
     * Get all the databaseMigrations.
     *
     * @return the list of entities.
     */
    List<DatabaseMigration> findAll();


    /**
     * Get the "id" databaseMigration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DatabaseMigration> findOne(Long id);

    /**
     * Delete the "id" databaseMigration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the databaseMigration corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<DatabaseMigration> search(String query);
}
