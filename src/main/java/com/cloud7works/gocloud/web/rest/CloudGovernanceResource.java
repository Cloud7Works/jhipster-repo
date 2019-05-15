package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.domain.CloudGovernance;
import com.cloud7works.gocloud.service.CloudGovernanceService;
import com.cloud7works.gocloud.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.cloud7works.gocloud.domain.CloudGovernance}.
 */
@RestController
@RequestMapping("/api")
public class CloudGovernanceResource {

    private final Logger log = LoggerFactory.getLogger(CloudGovernanceResource.class);

    private static final String ENTITY_NAME = "cloudGovernance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CloudGovernanceService cloudGovernanceService;

    public CloudGovernanceResource(CloudGovernanceService cloudGovernanceService) {
        this.cloudGovernanceService = cloudGovernanceService;
    }

    /**
     * {@code POST  /cloud-governances} : Create a new cloudGovernance.
     *
     * @param cloudGovernance the cloudGovernance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cloudGovernance, or with status {@code 400 (Bad Request)} if the cloudGovernance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cloud-governances")
    public ResponseEntity<CloudGovernance> createCloudGovernance(@RequestBody CloudGovernance cloudGovernance) throws URISyntaxException {
        log.debug("REST request to save CloudGovernance : {}", cloudGovernance);
        if (cloudGovernance.getId() != null) {
            throw new BadRequestAlertException("A new cloudGovernance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CloudGovernance result = cloudGovernanceService.save(cloudGovernance);
        return ResponseEntity.created(new URI("/api/cloud-governances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cloud-governances} : Updates an existing cloudGovernance.
     *
     * @param cloudGovernance the cloudGovernance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cloudGovernance,
     * or with status {@code 400 (Bad Request)} if the cloudGovernance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cloudGovernance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cloud-governances")
    public ResponseEntity<CloudGovernance> updateCloudGovernance(@RequestBody CloudGovernance cloudGovernance) throws URISyntaxException {
        log.debug("REST request to update CloudGovernance : {}", cloudGovernance);
        if (cloudGovernance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CloudGovernance result = cloudGovernanceService.save(cloudGovernance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cloudGovernance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cloud-governances} : get all the cloudGovernances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cloudGovernances in body.
     */
    @GetMapping("/cloud-governances")
    public List<CloudGovernance> getAllCloudGovernances() {
        log.debug("REST request to get all CloudGovernances");
        return cloudGovernanceService.findAll();
    }

    /**
     * {@code GET  /cloud-governances/:id} : get the "id" cloudGovernance.
     *
     * @param id the id of the cloudGovernance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cloudGovernance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cloud-governances/{id}")
    public ResponseEntity<CloudGovernance> getCloudGovernance(@PathVariable Long id) {
        log.debug("REST request to get CloudGovernance : {}", id);
        Optional<CloudGovernance> cloudGovernance = cloudGovernanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cloudGovernance);
    }

    /**
     * {@code DELETE  /cloud-governances/:id} : delete the "id" cloudGovernance.
     *
     * @param id the id of the cloudGovernance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cloud-governances/{id}")
    public ResponseEntity<Void> deleteCloudGovernance(@PathVariable Long id) {
        log.debug("REST request to delete CloudGovernance : {}", id);
        cloudGovernanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/cloud-governances?query=:query} : search for the cloudGovernance corresponding
     * to the query.
     *
     * @param query the query of the cloudGovernance search.
     * @return the result of the search.
     */
    @GetMapping("/_search/cloud-governances")
    public List<CloudGovernance> searchCloudGovernances(@RequestParam String query) {
        log.debug("REST request to search CloudGovernances for query {}", query);
        return cloudGovernanceService.search(query);
    }

}
