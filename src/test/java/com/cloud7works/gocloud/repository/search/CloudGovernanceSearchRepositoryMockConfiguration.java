package com.cloud7works.gocloud.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CloudGovernanceSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CloudGovernanceSearchRepositoryMockConfiguration {

    @MockBean
    private CloudGovernanceSearchRepository mockCloudGovernanceSearchRepository;

}
