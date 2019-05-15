package com.cloud7works.gocloud.service.impl;

import com.cloud7works.gocloud.service.DatabaseMigrationService;
import com.cloud7works.gocloud.domain.DatabaseMigration;
import com.cloud7works.gocloud.repository.DatabaseMigrationRepository;
import com.cloud7works.gocloud.repository.search.DatabaseMigrationSearchRepository;
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
 * Service Implementation for managing {@link DatabaseMigration}.
 */
@Service
@Transactional
public class DatabaseMigrationServiceImpl implements DatabaseMigrationService {

    private final Logger log = LoggerFactory.getLogger(DatabaseMigrationServiceImpl.class);

    private final DatabaseMigrationRepository databaseMigrationRepository;

    private final DatabaseMigrationSearchRepository databaseMigrationSearchRepository;

    public DatabaseMigrationServiceImpl(DatabaseMigrationRepository databaseMigrationRepository, DatabaseMigrationSearchRepository databaseMigrationSearchRepository) {
        this.databaseMigrationRepository = databaseMigrationRepository;
        this.databaseMigrationSearchRepository = databaseMigrationSearchRepository;
    }

    /**
     * Save a databaseMigration.
     *
     * @param databaseMigration the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DatabaseMigration save(DatabaseMigration databaseMigration) {
        log.debug("Request to save DatabaseMigration : {}", databaseMigration);
        DatabaseMigration result = databaseMigrationRepository.save(databaseMigration);
        databaseMigrationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the databaseMigrations.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DatabaseMigration> findAll() {
        log.debug("Request to get all DatabaseMigrations");
        return databaseMigrationRepository.findAll();
    }


    /**
     * Get one databaseMigration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DatabaseMigration> findOne(Long id) {
        log.debug("Request to get DatabaseMigration : {}", id);
        return databaseMigrationRepository.findById(id);
    }

    /**
     * Delete the databaseMigration by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DatabaseMigration : {}", id);
        databaseMigrationRepository.deleteById(id);
        databaseMigrationSearchRepository.deleteById(id);
    }

    /**
     * Search for the databaseMigration corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DatabaseMigration> search(String query) {
        log.debug("Request to search DatabaseMigrations for query {}", query);
        return StreamSupport
            .stream(databaseMigrationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
