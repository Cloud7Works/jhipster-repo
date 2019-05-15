package com.cloud7works.gocloud.repository;

import com.cloud7works.gocloud.domain.Credential;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Credential entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

}
