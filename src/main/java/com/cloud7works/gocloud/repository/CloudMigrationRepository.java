package com.cloud7works.gocloud.repository;

import com.cloud7works.gocloud.domain.CloudMigration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CloudMigration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CloudMigrationRepository extends JpaRepository<CloudMigration, Long> {

}
