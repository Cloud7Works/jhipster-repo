package com.cloud7works.gocloud.service;

import com.cloud7works.gocloud.domain.CloudAssessment;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link CloudAssessment}.
 */
public interface CloudAssessmentService {

    /**
     * Save a cloudAssessment.
     *
     * @param cloudAssessment the entity to save.
     * @return the persisted entity.
     */
    CloudAssessment save(CloudAssessment cloudAssessment);

    /**
     * Get all the cloudAssessments.
     *
     * @return the list of entities.
     */
    List<CloudAssessment> findAll();


    /**
     * Get the "id" cloudAssessment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CloudAssessment> findOne(Long id);

    /**
     * Delete the "id" cloudAssessment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cloudAssessment corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<CloudAssessment> search(String query);
}
