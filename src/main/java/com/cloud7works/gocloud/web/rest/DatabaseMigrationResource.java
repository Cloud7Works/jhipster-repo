package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.domain.DatabaseMigration;
import com.cloud7works.gocloud.service.DatabaseMigrationService;
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
 * REST controller for managing {@link com.cloud7works.gocloud.domain.DatabaseMigration}.
 */
@RestController
@RequestMapping("/api")
public class DatabaseMigrationResource {

    private final Logger log = LoggerFactory.getLogger(DatabaseMigrationResource.class);

    private static final String ENTITY_NAME = "databaseMigration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DatabaseMigrationService databaseMigrationService;

    public DatabaseMigrationResource(DatabaseMigrationService databaseMigrationService) {
        this.databaseMigrationService = databaseMigrationService;
    }

    /**
     * {@code POST  /database-migrations} : Create a new databaseMigration.
     *
     * @param databaseMigration the databaseMigration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new databaseMigration, or with status {@code 400 (Bad Request)} if the databaseMigration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/database-migrations")
    public ResponseEntity<DatabaseMigration> createDatabaseMigration(@RequestBody DatabaseMigration databaseMigration) throws URISyntaxException {
        log.debug("REST request to save DatabaseMigration : {}", databaseMigration);
        if (databaseMigration.getId() != null) {
            throw new BadRequestAlertException("A new databaseMigration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DatabaseMigration result = databaseMigrationService.save(databaseMigration);
        return ResponseEntity.created(new URI("/api/database-migrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /database-migrations} : Updates an existing databaseMigration.
     *
     * @param databaseMigration the databaseMigration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated databaseMigration,
     * or with status {@code 400 (Bad Request)} if the databaseMigration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the databaseMigration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/database-migrations")
    public ResponseEntity<DatabaseMigration> updateDatabaseMigration(@RequestBody DatabaseMigration databaseMigration) throws URISyntaxException {
        log.debug("REST request to update DatabaseMigration : {}", databaseMigration);
        if (databaseMigration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DatabaseMigration result = databaseMigrationService.save(databaseMigration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, databaseMigration.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /database-migrations} : get all the databaseMigrations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of databaseMigrations in body.
     */
    @GetMapping("/database-migrations")
    public List<DatabaseMigration> getAllDatabaseMigrations() {
        log.debug("REST request to get all DatabaseMigrations");
        return databaseMigrationService.findAll();
    }

    /**
     * {@code GET  /database-migrations/:id} : get the "id" databaseMigration.
     *
     * @param id the id of the databaseMigration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the databaseMigration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/database-migrations/{id}")
    public ResponseEntity<DatabaseMigration> getDatabaseMigration(@PathVariable Long id) {
        log.debug("REST request to get DatabaseMigration : {}", id);
        Optional<DatabaseMigration> databaseMigration = databaseMigrationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(databaseMigration);
    }

    /**
     * {@code DELETE  /database-migrations/:id} : delete the "id" databaseMigration.
     *
     * @param id the id of the databaseMigration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/database-migrations/{id}")
    public ResponseEntity<Void> deleteDatabaseMigration(@PathVariable Long id) {
        log.debug("REST request to delete DatabaseMigration : {}", id);
        databaseMigrationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/database-migrations?query=:query} : search for the databaseMigration corresponding
     * to the query.
     *
     * @param query the query of the databaseMigration search.
     * @return the result of the search.
     */
    @GetMapping("/_search/database-migrations")
    public List<DatabaseMigration> searchDatabaseMigrations(@RequestParam String query) {
        log.debug("REST request to search DatabaseMigrations for query {}", query);
        return databaseMigrationService.search(query);
    }

}
