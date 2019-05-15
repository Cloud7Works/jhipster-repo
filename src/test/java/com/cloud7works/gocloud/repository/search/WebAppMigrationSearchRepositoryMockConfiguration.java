package com.cloud7works.gocloud.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link WebAppMigrationSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class WebAppMigrationSearchRepositoryMockConfiguration {

    @MockBean
    private WebAppMigrationSearchRepository mockWebAppMigrationSearchRepository;

}
