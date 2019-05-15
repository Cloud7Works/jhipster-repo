package com.cloud7works.gocloud.service.impl;

import com.cloud7works.gocloud.service.ApplicationService;
import com.cloud7works.gocloud.domain.Application;
import com.cloud7works.gocloud.repository.ApplicationRepository;
import com.cloud7works.gocloud.repository.search.ApplicationSearchRepository;
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
 * Service Implementation for managing {@link Application}.
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final ApplicationRepository applicationRepository;

    private final ApplicationSearchRepository applicationSearchRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ApplicationSearchRepository applicationSearchRepository) {
        this.applicationRepository = applicationRepository;
        this.applicationSearchRepository = applicationSearchRepository;
    }

    /**
     * Save a application.
     *
     * @param application the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Application save(Application application) {
        log.debug("Request to save Application : {}", application);
        Application result = applicationRepository.save(application);
        applicationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the applications.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Application> findAll() {
        log.debug("Request to get all Applications");
        return applicationRepository.findAll();
    }


    /**
     * Get one application by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Application> findOne(Long id) {
        log.debug("Request to get Application : {}", id);
        return applicationRepository.findById(id);
    }

    /**
     * Delete the application by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Application : {}", id);
        applicationRepository.deleteById(id);
        applicationSearchRepository.deleteById(id);
    }

    /**
     * Search for the application corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Application> search(String query) {
        log.debug("Request to search Applications for query {}", query);
        return StreamSupport
            .stream(applicationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
