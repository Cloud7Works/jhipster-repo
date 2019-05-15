package com.cloud7works.gocloud.repository.search;

import com.cloud7works.gocloud.domain.DatabaseMigration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DatabaseMigration} entity.
 */
public interface DatabaseMigrationSearchRepository extends ElasticsearchRepository<DatabaseMigration, Long> {
}
