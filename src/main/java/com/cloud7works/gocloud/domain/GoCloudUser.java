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
    private CloudAssessment user;

    @OneToMany(mappedBy = "goCloudUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Credential> users = new HashSet<>();

    @OneToMany(mappedBy = "goCloudUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CloudGovernance> users = new HashSet<>();

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

    public CloudAssessment getUser() {
        return user;
    }

    public GoCloudUser user(CloudAssessment cloudAssessment) {
        this.user = cloudAssessment;
        return this;
    }

    public void setUser(CloudAssessment cloudAssessment) {
        this.user = cloudAssessment;
    }

    public Set<Credential> getUsers() {
        return users;
    }

    public GoCloudUser users(Set<Credential> credentials) {
        this.users = credentials;
        return this;
    }

    public GoCloudUser addUser(Credential credential) {
        this.users.add(credential);
        credential.setGoCloudUser(this);
        return this;
    }

    public GoCloudUser removeUser(Credential credential) {
        this.users.remove(credential);
        credential.setGoCloudUser(null);
        return this;
    }

    public void setUsers(Set<Credential> credentials) {
        this.users = credentials;
    }

    public Set<CloudGovernance> getUsers() {
        return users;
    }

    public GoCloudUser users(Set<CloudGovernance> cloudGovernances) {
        this.users = cloudGovernances;
        return this;
    }

    public GoCloudUser addUser(CloudGovernance cloudGovernance) {
        this.users.add(cloudGovernance);
        cloudGovernance.setGoCloudUser(this);
        return this;
    }

    public GoCloudUser removeUser(CloudGovernance cloudGovernance) {
        this.users.remove(cloudGovernance);
        cloudGovernance.setGoCloudUser(null);
        return this;
    }

    public void setUsers(Set<CloudGovernance> cloudGovernances) {
        this.users = cloudGovernances;
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
