package com.cloud7works.gocloud.repository;

import com.cloud7works.gocloud.domain.WebAppMigration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WebAppMigration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WebAppMigrationRepository extends JpaRepository<WebAppMigration, Long> {

}
