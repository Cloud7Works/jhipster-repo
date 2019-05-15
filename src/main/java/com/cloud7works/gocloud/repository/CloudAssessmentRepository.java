package com.cloud7works.gocloud.repository;

import com.cloud7works.gocloud.domain.CloudAssessment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CloudAssessment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CloudAssessmentRepository extends JpaRepository<CloudAssessment, Long> {

}
