package com.cloud7works.gocloud.service;

import com.cloud7works.gocloud.domain.CloudGovernance;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link CloudGovernance}.
 */
public interface CloudGovernanceService {

    /**
     * Save a cloudGovernance.
     *
     * @param cloudGovernance the entity to save.
     * @return the persisted entity.
     */
    CloudGovernance save(CloudGovernance cloudGovernance);

    /**
     * Get all the cloudGovernances.
     *
     * @return the list of entities.
     */
    List<CloudGovernance> findAll();


    /**
     * Get the "id" cloudGovernance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CloudGovernance> findOne(Long id);

    /**
     * Delete the "id" cloudGovernance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cloudGovernance corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<CloudGovernance> search(String query);
}
