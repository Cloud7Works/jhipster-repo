package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.domain.WebAppMigration;
import com.cloud7works.gocloud.service.WebAppMigrationService;
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
 * REST controller for managing {@link com.cloud7works.gocloud.domain.WebAppMigration}.
 */
@RestController
@RequestMapping("/api")
public class WebAppMigrationResource {

    private final Logger log = LoggerFactory.getLogger(WebAppMigrationResource.class);

    private static final String ENTITY_NAME = "webAppMigration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WebAppMigrationService webAppMigrationService;

    public WebAppMigrationResource(WebAppMigrationService webAppMigrationService) {
        this.webAppMigrationService = webAppMigrationService;
    }

    /**
     * {@code POST  /web-app-migrations} : Create a new webAppMigration.
     *
     * @param webAppMigration the webAppMigration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new webAppMigration, or with status {@code 400 (Bad Request)} if the webAppMigration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/web-app-migrations")
    public ResponseEntity<WebAppMigration> createWebAppMigration(@RequestBody WebAppMigration webAppMigration) throws URISyntaxException {
        log.debug("REST request to save WebAppMigration : {}", webAppMigration);
        if (webAppMigration.getId() != null) {
            throw new BadRequestAlertException("A new webAppMigration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WebAppMigration result = webAppMigrationService.save(webAppMigration);
        return ResponseEntity.created(new URI("/api/web-app-migrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /web-app-migrations} : Updates an existing webAppMigration.
     *
     * @param webAppMigration the webAppMigration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated webAppMigration,
     * or with status {@code 400 (Bad Request)} if the webAppMigration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the webAppMigration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/web-app-migrations")
    public ResponseEntity<WebAppMigration> updateWebAppMigration(@RequestBody WebAppMigration webAppMigration) throws URISyntaxException {
        log.debug("REST request to update WebAppMigration : {}", webAppMigration);
        if (webAppMigration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WebAppMigration result = webAppMigrationService.save(webAppMigration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, webAppMigration.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /web-app-migrations} : get all the webAppMigrations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of webAppMigrations in body.
     */
    @GetMapping("/web-app-migrations")
    public List<WebAppMigration> getAllWebAppMigrations() {
        log.debug("REST request to get all WebAppMigrations");
        return webAppMigrationService.findAll();
    }

    /**
     * {@code GET  /web-app-migrations/:id} : get the "id" webAppMigration.
     *
     * @param id the id of the webAppMigration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the webAppMigration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/web-app-migrations/{id}")
    public ResponseEntity<WebAppMigration> getWebAppMigration(@PathVariable Long id) {
        log.debug("REST request to get WebAppMigration : {}", id);
        Optional<WebAppMigration> webAppMigration = webAppMigrationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(webAppMigration);
    }

    /**
     * {@code DELETE  /web-app-migrations/:id} : delete the "id" webAppMigration.
     *
     * @param id the id of the webAppMigration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/web-app-migrations/{id}")
    public ResponseEntity<Void> deleteWebAppMigration(@PathVariable Long id) {
        log.debug("REST request to delete WebAppMigration : {}", id);
        webAppMigrationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/web-app-migrations?query=:query} : search for the webAppMigration corresponding
     * to the query.
     *
     * @param query the query of the webAppMigration search.
     * @return the result of the search.
     */
    @GetMapping("/_search/web-app-migrations")
    public List<WebAppMigration> searchWebAppMigrations(@RequestParam String query) {
        log.debug("REST request to search WebAppMigrations for query {}", query);
        return webAppMigrationService.search(query);
    }

}
