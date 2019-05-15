package com.cloud7works.gocloud.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

import com.cloud7works.gocloud.domain.enumeration.ApplicationType;

import com.cloud7works.gocloud.domain.enumeration.Environment;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "application")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "application_id")
    private String applicationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private ApplicationType type;

    @Column(name = "git_url")
    private String gitUrl;

    @Column(name = "stack_name")
    private String stackName;

    @Enumerated(EnumType.STRING)
    @Column(name = "environment")
    private Environment environment;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public Application applicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public ApplicationType getType() {
        return type;
    }

    public Application type(ApplicationType type) {
        this.type = type;
        return this;
    }

    public void setType(ApplicationType type) {
        this.type = type;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public Application gitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
        return this;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getStackName() {
        return stackName;
    }

    public Application stackName(String stackName) {
        this.stackName = stackName;
        return this;
    }

    public void setStackName(String stackName) {
        this.stackName = stackName;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Application environment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Application)) {
            return false;
        }
        return id != null && id.equals(((Application) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", applicationId='" + getApplicationId() + "'" +
            ", type='" + getType() + "'" +
            ", gitUrl='" + getGitUrl() + "'" +
            ", stackName='" + getStackName() + "'" +
            ", environment='" + getEnvironment() + "'" +
            "}";
    }
}
