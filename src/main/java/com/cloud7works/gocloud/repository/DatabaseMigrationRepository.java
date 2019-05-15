package com.cloud7works.gocloud.repository;

import com.cloud7works.gocloud.domain.DatabaseMigration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DatabaseMigration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DatabaseMigrationRepository extends JpaRepository<DatabaseMigration, Long> {

}
