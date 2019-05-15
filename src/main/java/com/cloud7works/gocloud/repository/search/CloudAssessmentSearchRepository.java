package com.cloud7works.gocloud.repository.search;

import com.cloud7works.gocloud.domain.CloudAssessment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CloudAssessment} entity.
 */
public interface CloudAssessmentSearchRepository extends ElasticsearchRepository<CloudAssessment, Long> {
}
