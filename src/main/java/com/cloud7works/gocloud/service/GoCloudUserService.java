package com.cloud7works.gocloud.service;

import com.cloud7works.gocloud.domain.GoCloudUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link GoCloudUser}.
 */
public interface GoCloudUserService {

    /**
     * Save a goCloudUser.
     *
     * @param goCloudUser the entity to save.
     * @return the persisted entity.
     */
    GoCloudUser save(GoCloudUser goCloudUser);

    /**
     * Get all the goCloudUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GoCloudUser> findAll(Pageable pageable);


    /**
     * Get the "id" goCloudUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GoCloudUser> findOne(Long id);

    /**
     * Delete the "id" goCloudUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the goCloudUser corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GoCloudUser> search(String query, Pageable pageable);
}
