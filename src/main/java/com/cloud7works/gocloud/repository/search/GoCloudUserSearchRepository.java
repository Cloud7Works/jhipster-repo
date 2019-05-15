package com.cloud7works.gocloud.repository.search;

import com.cloud7works.gocloud.domain.GoCloudUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link GoCloudUser} entity.
 */
public interface GoCloudUserSearchRepository extends ElasticsearchRepository<GoCloudUser, Long> {
}
