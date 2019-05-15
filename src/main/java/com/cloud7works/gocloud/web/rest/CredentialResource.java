package com.cloud7works.gocloud.web.rest;

import com.cloud7works.gocloud.domain.Credential;
import com.cloud7works.gocloud.service.CredentialService;
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
 * REST controller for managing {@link com.cloud7works.gocloud.domain.Credential}.
 */
@RestController
@RequestMapping("/api")
public class CredentialResource {

    private final Logger log = LoggerFactory.getLogger(CredentialResource.class);

    private static final String ENTITY_NAME = "credential";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CredentialService credentialService;

    public CredentialResource(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    /**
     * {@code POST  /credentials} : Create a new credential.
     *
     * @param credential the credential to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new credential, or with status {@code 400 (Bad Request)} if the credential has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/credentials")
    public ResponseEntity<Credential> createCredential(@RequestBody Credential credential) throws URISyntaxException {
        log.debug("REST request to save Credential : {}", credential);
        if (credential.getId() != null) {
            throw new BadRequestAlertException("A new credential cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Credential result = credentialService.save(credential);
        return ResponseEntity.created(new URI("/api/credentials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /credentials} : Updates an existing credential.
     *
     * @param credential the credential to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated credential,
     * or with status {@code 400 (Bad Request)} if the credential is not valid,
     * or with status {@code 500 (Internal Server Error)} if the credential couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/credentials")
    public ResponseEntity<Credential> updateCredential(@RequestBody Credential credential) throws URISyntaxException {
        log.debug("REST request to update Credential : {}", credential);
        if (credential.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Credential result = credentialService.save(credential);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, credential.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /credentials} : get all the credentials.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of credentials in body.
     */
    @GetMapping("/credentials")
    public List<Credential> getAllCredentials() {
        log.debug("REST request to get all Credentials");
        return credentialService.findAll();
    }

    /**
     * {@code GET  /credentials/:id} : get the "id" credential.
     *
     * @param id the id of the credential to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the credential, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/credentials/{id}")
    public ResponseEntity<Credential> getCredential(@PathVariable Long id) {
        log.debug("REST request to get Credential : {}", id);
        Optional<Credential> credential = credentialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(credential);
    }

    /**
     * {@code DELETE  /credentials/:id} : delete the "id" credential.
     *
     * @param id the id of the credential to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/credentials/{id}")
    public ResponseEntity<Void> deleteCredential(@PathVariable Long id) {
        log.debug("REST request to delete Credential : {}", id);
        credentialService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/credentials?query=:query} : search for the credential corresponding
     * to the query.
     *
     * @param query the query of the credential search.
     * @return the result of the search.
     */
    @GetMapping("/_search/credentials")
    public List<Credential> searchCredentials(@RequestParam String query) {
        log.debug("REST request to search Credentials for query {}", query);
        return credentialService.search(query);
    }

}
