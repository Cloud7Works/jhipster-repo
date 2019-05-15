package com.cloud7works.gocloud.service.impl;

import com.cloud7works.gocloud.service.GoCloudUserService;
import com.cloud7works.gocloud.domain.GoCloudUser;
import com.cloud7works.gocloud.repository.GoCloudUserRepository;
import com.cloud7works.gocloud.repository.search.GoCloudUserSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link GoCloudUser}.
 */
@Service
@Transactional
public class GoCloudUserServiceImpl implements GoCloudUserService {

    private final Logger log = LoggerFactory.getLogger(GoCloudUserServiceImpl.class);

    private final GoCloudUserRepository goCloudUserRepository;

    private final GoCloudUserSearchRepository goCloudUserSearchRepository;

    public GoCloudUserServiceImpl(GoCloudUserRepository goCloudUserRepository, GoCloudUserSearchRepository goCloudUserSearchRepository) {
        this.goCloudUserRepository = goCloudUserRepository;
        this.goCloudUserSearchRepository = goCloudUserSearchRepository;
    }

    /**
     * Save a goCloudUser.
     *
     * @param goCloudUser the entity to save.
     * @return the persisted entity.
     */
    @Override
    public GoCloudUser save(GoCloudUser goCloudUser) {
        log.debug("Request to save GoCloudUser : {}", goCloudUser);
        GoCloudUser result = goCloudUserRepository.save(goCloudUser);
        goCloudUserSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the goCloudUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GoCloudUser> findAll(Pageable pageable) {
        log.debug("Request to get all GoCloudUsers");
        return goCloudUserRepository.findAll(pageable);
    }


    /**
     * Get one goCloudUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GoCloudUser> findOne(Long id) {
        log.debug("Request to get GoCloudUser : {}", id);
        return goCloudUserRepository.findById(id);
    }

    /**
     * Delete the goCloudUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GoCloudUser : {}", id);
        goCloudUserRepository.deleteById(id);
        goCloudUserSearchRepository.deleteById(id);
    }

    /**
     * Search for the goCloudUser corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GoCloudUser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GoCloudUsers for query {}", query);
        return goCloudUserSearchRepository.search(queryStringQuery(query), pageable);    }
}
