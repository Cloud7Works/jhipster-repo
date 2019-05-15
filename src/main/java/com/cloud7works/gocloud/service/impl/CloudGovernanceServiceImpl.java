package com.cloud7works.gocloud.service.impl;

import com.cloud7works.gocloud.service.CloudGovernanceService;
import com.cloud7works.gocloud.domain.CloudGovernance;
import com.cloud7works.gocloud.repository.CloudGovernanceRepository;
import com.cloud7works.gocloud.repository.search.CloudGovernanceSearchRepository;
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
 * Service Implementation for managing {@link CloudGovernance}.
 */
@Service
@Transactional
public class CloudGovernanceServiceImpl implements CloudGovernanceService {

    private final Logger log = LoggerFactory.getLogger(CloudGovernanceServiceImpl.class);

    private final CloudGovernanceRepository cloudGovernanceRepository;

    private final CloudGovernanceSearchRepository cloudGovernanceSearchRepository;

    public CloudGovernanceServiceImpl(CloudGovernanceRepository cloudGovernanceRepository, CloudGovernanceSearchRepository cloudGovernanceSearchRepository) {
        this.cloudGovernanceRepository = cloudGovernanceRepository;
        this.cloudGovernanceSearchRepository = cloudGovernanceSearchRepository;
    }

    /**
     * Save a cloudGovernance.
     *
     * @param cloudGovernance the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CloudGovernance save(CloudGovernance cloudGovernance) {
        log.debug("Request to save CloudGovernance : {}", cloudGovernance);
        CloudGovernance result = cloudGovernanceRepository.save(cloudGovernance);
        cloudGovernanceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the cloudGovernances.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CloudGovernance> findAll() {
        log.debug("Request to get all CloudGovernances");
        return cloudGovernanceRepository.findAll();
    }


    /**
     * Get one cloudGovernance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CloudGovernance> findOne(Long id) {
        log.debug("Request to get CloudGovernance : {}", id);
        return cloudGovernanceRepository.findById(id);
    }

    /**
     * Delete the cloudGovernance by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CloudGovernance : {}", id);
        cloudGovernanceRepository.deleteById(id);
        cloudGovernanceSearchRepository.deleteById(id);
    }

    /**
     * Search for the cloudGovernance corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CloudGovernance> search(String query) {
        log.debug("Request to search CloudGovernances for query {}", query);
        return StreamSupport
            .stream(cloudGovernanceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
