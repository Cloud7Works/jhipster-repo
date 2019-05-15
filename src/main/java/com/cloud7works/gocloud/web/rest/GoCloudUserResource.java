package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.domain.GoCloudUser;
import com.cloud7works.gocloud.service.GoCloudUserService;
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
 * REST controller for managing {@link com.cloud7works.gocloud.domain.GoCloudUser}.
 */
@RestController
@RequestMapping("/api")
public class GoCloudUserResource {

    private final Logger log = LoggerFactory.getLogger(GoCloudUserResource.class);

    private static final String ENTITY_NAME = "goCloudUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoCloudUserService goCloudUserService;

    public GoCloudUserResource(GoCloudUserService goCloudUserService) {
        this.goCloudUserService = goCloudUserService;
    }

    /**
     * {@code POST  /go-cloud-users} : Create a new goCloudUser.
     *
     * @param goCloudUser the goCloudUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new goCloudUser, or with status {@code 400 (Bad Request)} if the goCloudUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/go-cloud-users")
    public ResponseEntity<GoCloudUser> createGoCloudUser(@RequestBody GoCloudUser goCloudUser) throws URISyntaxException {
        log.debug("REST request to save GoCloudUser : {}", goCloudUser);
        if (goCloudUser.getId() != null) {
            throw new BadRequestAlertException("A new goCloudUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoCloudUser result = goCloudUserService.save(goCloudUser);
        return ResponseEntity.created(new URI("/api/go-cloud-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /go-cloud-users} : Updates an existing goCloudUser.
     *
     * @param goCloudUser the goCloudUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goCloudUser,
     * or with status {@code 400 (Bad Request)} if the goCloudUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the goCloudUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/go-cloud-users")
    public ResponseEntity<GoCloudUser> updateGoCloudUser(@RequestBody GoCloudUser goCloudUser) throws URISyntaxException {
        log.debug("REST request to update GoCloudUser : {}", goCloudUser);
        if (goCloudUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GoCloudUser result = goCloudUserService.save(goCloudUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goCloudUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /go-cloud-users} : get all the goCloudUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of goCloudUsers in body.
     */
    @GetMapping("/go-cloud-users")
    public ResponseEntity<List<GoCloudUser>> getAllGoCloudUsers(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of GoCloudUsers");
        Page<GoCloudUser> page = goCloudUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /go-cloud-users/:id} : get the "id" goCloudUser.
     *
     * @param id the id of the goCloudUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the goCloudUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/go-cloud-users/{id}")
    public ResponseEntity<GoCloudUser> getGoCloudUser(@PathVariable Long id) {
        log.debug("REST request to get GoCloudUser : {}", id);
        Optional<GoCloudUser> goCloudUser = goCloudUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(goCloudUser);
    }

    /**
     * {@code DELETE  /go-cloud-users/:id} : delete the "id" goCloudUser.
     *
     * @param id the id of the goCloudUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/go-cloud-users/{id}")
    public ResponseEntity<Void> deleteGoCloudUser(@PathVariable Long id) {
        log.debug("REST request to delete GoCloudUser : {}", id);
        goCloudUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/go-cloud-users?query=:query} : search for the goCloudUser corresponding
     * to the query.
     *
     * @param query the query of the goCloudUser search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/go-cloud-users")
    public ResponseEntity<List<GoCloudUser>> searchGoCloudUsers(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of GoCloudUsers for query {}", query);
        Page<GoCloudUser> page = goCloudUserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
