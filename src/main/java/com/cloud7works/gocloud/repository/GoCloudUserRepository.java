package com.cloud7works.gocloud.repository;

import com.cloud7works.gocloud.domain.GoCloudUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GoCloudUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoCloudUserRepository extends JpaRepository<GoCloudUser, Long> {

}
