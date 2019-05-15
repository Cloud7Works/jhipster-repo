package com.cloud7works.gocloud.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CloudMigration.
 */
@Entity
@Table(name = "cloud_migration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cloudmigration")
public class CloudMigration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "cloud_migration_id")
    private String cloudMigrationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public CloudMigration userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCloudMigrationId() {
        return cloudMigrationId;
    }

    public CloudMigration cloudMigrationId(String cloudMigrationId) {
        this.cloudMigrationId = cloudMigrationId;
        return this;
    }

    public void setCloudMigrationId(String cloudMigrationId) {
        this.cloudMigrationId = cloudMigrationId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CloudMigration)) {
            return false;
        }
        return id != null && id.equals(((CloudMigration) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CloudMigration{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", cloudMigrationId='" + getCloudMigrationId() + "'" +
            "}";
    }
}
