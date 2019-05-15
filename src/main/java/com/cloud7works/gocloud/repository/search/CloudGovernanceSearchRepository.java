package com.cloud7works.gocloud.repository.search;

import com.cloud7works.gocloud.domain.CloudGovernance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CloudGovernance} entity.
 */
public interface CloudGovernanceSearchRepository extends ElasticsearchRepository<CloudGovernance, Long> {
}
