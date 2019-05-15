package com.cloud7works.gocloud.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Credential.
 */
@Entity
@Table(name = "credential")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "credential")
public class Credential implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "account_id")
    private String accountID;

    @Column(name = "cloud_provider")
    private String cloudProvider;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "access_key")
    private String accessKey;

    @Column(name = "region")
    private String region;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    @JsonIgnoreProperties("credentials")
    private GoCloudUser goCloudUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountID() {
        return accountID;
    }

    public Credential accountID(String accountID) {
        this.accountID = accountID;
        return this;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getCloudProvider() {
        return cloudProvider;
    }

    public Credential cloudProvider(String cloudProvider) {
        this.cloudProvider = cloudProvider;
        return this;
    }

    public void setCloudProvider(String cloudProvider) {
        this.cloudProvider = cloudProvider;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public Credential secretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public Credential accessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getRegion() {
        return region;
    }

    public Credential region(String region) {
        this.region = region;
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUserId() {
        return userId;
    }

    public Credential userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GoCloudUser getGoCloudUser() {
        return goCloudUser;
    }

    public Credential goCloudUser(GoCloudUser goCloudUser) {
        this.goCloudUser = goCloudUser;
        return this;
    }

    public void setGoCloudUser(GoCloudUser goCloudUser) {
        this.goCloudUser = goCloudUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Credential)) {
            return false;
        }
        return id != null && id.equals(((Credential) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Credential{" +
            "id=" + getId() +
            ", accountID='" + getAccountID() + "'" +
            ", cloudProvider='" + getCloudProvider() + "'" +
            ", secretKey='" + getSecretKey() + "'" +
            ", accessKey='" + getAccessKey() + "'" +
            ", region='" + getRegion() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
