package com.cloud7works.gocloud.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A GoCloudUser.
 */
@Entity
@Table(name = "go_cloud_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "goclouduser")
public class GoCloudUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "cloud_or_not")
    private String cloudOrNot;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(unique = true)
    private CloudAssessment assessment;

    @OneToMany(mappedBy = "goCloudUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Credential> credentials = new HashSet<>();

    @OneToMany(mappedBy = "goCloudUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CloudGovernance> governances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public GoCloudUser companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public GoCloudUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public GoCloudUser lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCloudOrNot() {
        return cloudOrNot;
    }

    public GoCloudUser cloudOrNot(String cloudOrNot) {
        this.cloudOrNot = cloudOrNot;
        return this;
    }

    public void setCloudOrNot(String cloudOrNot) {
        this.cloudOrNot = cloudOrNot;
    }

    public String getUserId() {
        return userId;
    }

    public GoCloudUser userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public GoCloudUser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CloudAssessment getAssessment() {
        return assessment;
    }

    public GoCloudUser assessment(CloudAssessment cloudAssessment) {
        this.assessment = cloudAssessment;
        return this;
    }

    public void setAssessment(CloudAssessment cloudAssessment) {
        this.assessment = cloudAssessment;
    }

    public Set<Credential> getCredentials() {
        return credentials;
    }

    public GoCloudUser credentials(Set<Credential> credentials) {
        this.credentials = credentials;
        return this;
    }

    public GoCloudUser addCredential(Credential credential) {
        this.credentials.add(credential);
        credential.setGoCloudUser(this);
        return this;
    }

    public GoCloudUser removeCredential(Credential credential) {
        this.credentials.remove(credential);
        credential.setGoCloudUser(null);
        return this;
    }

    public void setCredentials(Set<Credential> credentials) {
        this.credentials = credentials;
    }

    public Set<CloudGovernance> getGovernances() {
        return governances;
    }

    public GoCloudUser governances(Set<CloudGovernance> cloudGovernances) {
        this.governances = cloudGovernances;
        return this;
    }

    public GoCloudUser addGovernance(CloudGovernance cloudGovernance) {
        this.governances.add(cloudGovernance);
        cloudGovernance.setGoCloudUser(this);
        return this;
    }

    public GoCloudUser removeGovernance(CloudGovernance cloudGovernance) {
        this.governances.remove(cloudGovernance);
        cloudGovernance.setGoCloudUser(null);
        return this;
    }

    public void setGovernances(Set<CloudGovernance> cloudGovernances) {
        this.governances = cloudGovernances;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GoCloudUser)) {
            return false;
        }
        return id != null && id.equals(((GoCloudUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GoCloudUser{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", cloudOrNot='" + getCloudOrNot() + "'" +
            ", userId='" + getUserId() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
