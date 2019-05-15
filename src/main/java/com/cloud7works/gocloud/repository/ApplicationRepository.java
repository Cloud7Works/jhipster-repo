package com.cloud7works.gocloud.repository;

import com.cloud7works.gocloud.domain.Application;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Application entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

}
