package com.cloud7works.gocloud.repository.search;

import com.cloud7works.gocloud.domain.CloudMigration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CloudMigration} entity.
 */
public interface CloudMigrationSearchRepository extends ElasticsearchRepository<CloudMigration, Long> {
}
