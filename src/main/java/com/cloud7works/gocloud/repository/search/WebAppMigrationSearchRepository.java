package com.cloud7works.gocloud.repository.search;

import com.cloud7works.gocloud.domain.WebAppMigration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link WebAppMigration} entity.
 */
public interface WebAppMigrationSearchRepository extends ElasticsearchRepository<WebAppMigration, Long> {
}
