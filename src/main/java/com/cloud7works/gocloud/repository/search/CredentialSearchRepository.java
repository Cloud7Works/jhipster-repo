package com.cloud7works.gocloud.repository.search;

import com.cloud7works.gocloud.domain.Credential;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Credential} entity.
 */
public interface CredentialSearchRepository extends ElasticsearchRepository<Credential, Long> {
}
