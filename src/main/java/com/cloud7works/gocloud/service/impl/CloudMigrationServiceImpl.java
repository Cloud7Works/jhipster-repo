package com.cloud7works.gocloud.service.impl;

import com.cloud7works.gocloud.service.CloudMigrationService;
import com.cloud7works.gocloud.domain.CloudMigration;
import com.cloud7works.gocloud.repository.CloudMigrationRepository;
import com.cloud7works.gocloud.repository.search.CloudMigrationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link CloudMigration}.
 */
@Service
@Transactional
public class CloudMigrationServiceImpl implements CloudMigrationService {

    private final Logger log = LoggerFactory.getLogger(CloudMigrationServiceImpl.class);

    private final CloudMigrationRepository cloudMigrationRepository;

    private final CloudMigrationSearchRepository cloudMigrationSearchRepository;

    public CloudMigrationServiceImpl(CloudMigrationRepository cloudMigrationRepository, CloudMigrationSearchRepository cloudMigrationSearchRepository) {
        this.cloudMigrationRepository = cloudMigrationRepository;
        this.cloudMigrationSearchRepository = cloudMigrationSearchRepository;
    }

    /**
     * Save a cloudMigration.
     *
     * @param cloudMigration the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CloudMigration save(CloudMigration cloudMigration) {
        log.debug("Request to save CloudMigration : {}", cloudMigration);
        CloudMigration result = cloudMigrationRepository.save(cloudMigration);
        cloudMigrationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the cloudMigrations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CloudMigration> findAll(Pageable pageable) {
        log.debug("Request to get all CloudMigrations");
        return cloudMigrationRepository.findAll(pageable);
    }


    /**
     * Get one cloudMigration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CloudMigration> findOne(Long id) {
        log.debug("Request to get CloudMigration : {}", id);
        return cloudMigrationRepository.findById(id);
    }

    /**
     * Delete the cloudMigration by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CloudMigration : {}", id);
        cloudMigrationRepository.deleteById(id);
        cloudMigrationSearchRepository.deleteById(id);
    }

    /**
     * Search for the cloudMigration corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CloudMigration> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CloudMigrations for query {}", query);
        return cloudMigrationSearchRepository.search(queryStringQuery(query), pageable);    }
}
