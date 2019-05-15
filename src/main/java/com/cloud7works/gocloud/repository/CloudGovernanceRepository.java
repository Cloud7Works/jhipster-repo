package com.cloud7works.gocloud.repository;

import com.cloud7works.gocloud.domain.CloudGovernance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CloudGovernance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CloudGovernanceRepository extends JpaRepository<CloudGovernance, Long> {

}
