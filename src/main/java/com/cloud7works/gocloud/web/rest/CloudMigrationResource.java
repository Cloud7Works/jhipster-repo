package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.domain.CloudMigration;
import com.cloud7works.gocloud.service.CloudMigrationService;
import com.cloud7works.gocloud.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.cloud7works.gocloud.domain.CloudMigration}.
 */
@RestController
@RequestMapping("/api")
public class CloudMigrationResource {

    private final Logger log = LoggerFactory.getLogger(CloudMigrationResource.class);

    private static final String ENTITY_NAME = "cloudMigration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CloudMigrationService cloudMigrationService;

    public CloudMigrationResource(CloudMigrationService cloudMigrationService) {
        this.cloudMigrationService = cloudMigrationService;
    }

    /**
     * {@code POST  /cloud-migrations} : Create a new cloudMigration.
     *
     * @param cloudMigration the cloudMigration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cloudMigration, or with status {@code 400 (Bad Request)} if the cloudMigration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cloud-migrations")
    public ResponseEntity<CloudMigration> createCloudMigration(@RequestBody CloudMigration cloudMigration) throws URISyntaxException {
        log.debug("REST request to save CloudMigration : {}", cloudMigration);
        if (cloudMigration.getId() != null) {
            throw new BadRequestAlertException("A new cloudMigration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CloudMigration result = cloudMigrationService.save(cloudMigration);
        return ResponseEntity.created(new URI("/api/cloud-migrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cloud-migrations} : Updates an existing cloudMigration.
     *
     * @param cloudMigration the cloudMigration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cloudMigration,
     * or with status {@code 400 (Bad Request)} if the cloudMigration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cloudMigration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cloud-migrations")
    public ResponseEntity<CloudMigration> updateCloudMigration(@RequestBody CloudMigration cloudMigration) throws URISyntaxException {
        log.debug("REST request to update CloudMigration : {}", cloudMigration);
        if (cloudMigration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CloudMigration result = cloudMigrationService.save(cloudMigration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cloudMigration.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cloud-migrations} : get all the cloudMigrations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cloudMigrations in body.
     */
    @GetMapping("/cloud-migrations")
    public ResponseEntity<List<CloudMigration>> getAllCloudMigrations(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of CloudMigrations");
        Page<CloudMigration> page = cloudMigrationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cloud-migrations/:id} : get the "id" cloudMigration.
     *
     * @param id the id of the cloudMigration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cloudMigration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cloud-migrations/{id}")
    public ResponseEntity<CloudMigration> getCloudMigration(@PathVariable Long id) {
        log.debug("REST request to get CloudMigration : {}", id);
        Optional<CloudMigration> cloudMigration = cloudMigrationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cloudMigration);
    }

    /**
     * {@code DELETE  /cloud-migrations/:id} : delete the "id" cloudMigration.
     *
     * @param id the id of the cloudMigration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cloud-migrations/{id}")
    public ResponseEntity<Void> deleteCloudMigration(@PathVariable Long id) {
        log.debug("REST request to delete CloudMigration : {}", id);
        cloudMigrationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/cloud-migrations?query=:query} : search for the cloudMigration corresponding
     * to the query.
     *
     * @param query the query of the cloudMigration search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/cloud-migrations")
    public ResponseEntity<List<CloudMigration>> searchCloudMigrations(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of CloudMigrations for query {}", query);
        Page<CloudMigration> page = cloudMigrationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
