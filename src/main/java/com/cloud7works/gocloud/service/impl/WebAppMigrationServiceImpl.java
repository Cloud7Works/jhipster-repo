package com.cloud7works.gocloud.service.impl;

import com.cloud7works.gocloud.service.WebAppMigrationService;
import com.cloud7works.gocloud.domain.WebAppMigration;
import com.cloud7works.gocloud.repository.WebAppMigrationRepository;
import com.cloud7works.gocloud.repository.search.WebAppMigrationSearchRepository;
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
 * Service Implementation for managing {@link WebAppMigration}.
 */
@Service
@Transactional
public class WebAppMigrationServiceImpl implements WebAppMigrationService {

    private final Logger log = LoggerFactory.getLogger(WebAppMigrationServiceImpl.class);

    private final WebAppMigrationRepository webAppMigrationRepository;

    private final WebAppMigrationSearchRepository webAppMigrationSearchRepository;

    public WebAppMigrationServiceImpl(WebAppMigrationRepository webAppMigrationRepository, WebAppMigrationSearchRepository webAppMigrationSearchRepository) {
        this.webAppMigrationRepository = webAppMigrationRepository;
        this.webAppMigrationSearchRepository = webAppMigrationSearchRepository;
    }

    /**
     * Save a webAppMigration.
     *
     * @param webAppMigration the entity to save.
     * @return the persisted entity.
     */
    @Override
    public WebAppMigration save(WebAppMigration webAppMigration) {
        log.debug("Request to save WebAppMigration : {}", webAppMigration);
        WebAppMigration result = webAppMigrationRepository.save(webAppMigration);
        webAppMigrationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the webAppMigrations.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<WebAppMigration> findAll() {
        log.debug("Request to get all WebAppMigrations");
        return webAppMigrationRepository.findAll();
    }


    /**
     * Get one webAppMigration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WebAppMigration> findOne(Long id) {
        log.debug("Request to get WebAppMigration : {}", id);
        return webAppMigrationRepository.findById(id);
    }

    /**
     * Delete the webAppMigration by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WebAppMigration : {}", id);
        webAppMigrationRepository.deleteById(id);
        webAppMigrationSearchRepository.deleteById(id);
    }

    /**
     * Search for the webAppMigration corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<WebAppMigration> search(String query) {
        log.debug("Request to search WebAppMigrations for query {}", query);
        return StreamSupport
            .stream(webAppMigrationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
