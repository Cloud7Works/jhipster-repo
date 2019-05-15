package com.cloud7works.gocloud.repository.search;

import com.cloud7works.gocloud.domain.Application;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Application} entity.
 */
public interface ApplicationSearchRepository extends ElasticsearchRepository<Application, Long> {
}
