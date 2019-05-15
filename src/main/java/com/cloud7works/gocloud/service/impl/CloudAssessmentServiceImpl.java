package com.cloud7works.gocloud.service.impl;

import com.cloud7works.gocloud.service.CloudAssessmentService;
import com.cloud7works.gocloud.domain.CloudAssessment;
import com.cloud7works.gocloud.repository.CloudAssessmentRepository;
import com.cloud7works.gocloud.repository.search.CloudAssessmentSearchRepository;
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
 * Service Implementation for managing {@link CloudAssessment}.
 */
@Service
@Transactional
public class CloudAssessmentServiceImpl implements CloudAssessmentService {

    private final Logger log = LoggerFactory.getLogger(CloudAssessmentServiceImpl.class);

    private final CloudAssessmentRepository cloudAssessmentRepository;

    private final CloudAssessmentSearchRepository cloudAssessmentSearchRepository;

    public CloudAssessmentServiceImpl(CloudAssessmentRepository cloudAssessmentRepository, CloudAssessmentSearchRepository cloudAssessmentSearchRepository) {
        this.cloudAssessmentRepository = cloudAssessmentRepository;
        this.cloudAssessmentSearchRepository = cloudAssessmentSearchRepository;
    }

    /**
     * Save a cloudAssessment.
     *
     * @param cloudAssessment the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CloudAssessment save(CloudAssessment cloudAssessment) {
        log.debug("Request to save CloudAssessment : {}", cloudAssessment);
        CloudAssessment result = cloudAssessmentRepository.save(cloudAssessment);
        cloudAssessmentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the cloudAssessments.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CloudAssessment> findAll() {
        log.debug("Request to get all CloudAssessments");
        return cloudAssessmentRepository.findAll();
    }


    /**
     * Get one cloudAssessment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CloudAssessment> findOne(Long id) {
        log.debug("Request to get CloudAssessment : {}", id);
        return cloudAssessmentRepository.findById(id);
    }

    /**
     * Delete the cloudAssessment by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CloudAssessment : {}", id);
        cloudAssessmentRepository.deleteById(id);
        cloudAssessmentSearchRepository.deleteById(id);
    }

    /**
     * Search for the cloudAssessment corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CloudAssessment> search(String query) {
        log.debug("Request to search CloudAssessments for query {}", query);
        return StreamSupport
            .stream(cloudAssessmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
