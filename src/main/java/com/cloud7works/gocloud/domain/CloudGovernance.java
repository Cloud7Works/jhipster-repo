package com.cloud7works.gocloud.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CloudGovernance.
 */
@Entity
@Table(name = "cloud_governance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cloudgovernance")
public class CloudGovernance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "governance_id")
    private String governanceId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "active_apps")
    private Integer activeApps;

    @Column(name = "in_active_apps")
    private Integer inActiveApps;

    @Column(name = "current_spend_across")
    private Integer currentSpendAcross;

    @Column(name = "estimated_spend_across")
    private Integer estimatedSpendAcross;

    @Column(name = "security_alerts_across")
    private String securityAlertsAcross;

    @ManyToOne
    @JsonIgnoreProperties("cloudGovernances")
    private GoCloudUser goCloudUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGovernanceId() {
        return governanceId;
    }

    public CloudGovernance governanceId(String governanceId) {
        this.governanceId = governanceId;
        return this;
    }

    public void setGovernanceId(String governanceId) {
        this.governanceId = governanceId;
    }

    public String getUserId() {
        return userId;
    }

    public CloudGovernance userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getActiveApps() {
        return activeApps;
    }

    public CloudGovernance activeApps(Integer activeApps) {
        this.activeApps = activeApps;
        return this;
    }

    public void setActiveApps(Integer activeApps) {
        this.activeApps = activeApps;
    }

    public Integer getInActiveApps() {
        return inActiveApps;
    }

    public CloudGovernance inActiveApps(Integer inActiveApps) {
        this.inActiveApps = inActiveApps;
        return this;
    }

    public void setInActiveApps(Integer inActiveApps) {
        this.inActiveApps = inActiveApps;
    }

    public Integer getCurrentSpendAcross() {
        return currentSpendAcross;
    }

    public CloudGovernance currentSpendAcross(Integer currentSpendAcross) {
        this.currentSpendAcross = currentSpendAcross;
        return this;
    }

    public void setCurrentSpendAcross(Integer currentSpendAcross) {
        this.currentSpendAcross = currentSpendAcross;
    }

    public Integer getEstimatedSpendAcross() {
        return estimatedSpendAcross;
    }

    public CloudGovernance estimatedSpendAcross(Integer estimatedSpendAcross) {
        this.estimatedSpendAcross = estimatedSpendAcross;
        return this;
    }

    public void setEstimatedSpendAcross(Integer estimatedSpendAcross) {
        this.estimatedSpendAcross = estimatedSpendAcross;
    }

    public String getSecurityAlertsAcross() {
        return securityAlertsAcross;
    }

    public CloudGovernance securityAlertsAcross(String securityAlertsAcross) {
        this.securityAlertsAcross = securityAlertsAcross;
        return this;
    }

    public void setSecurityAlertsAcross(String securityAlertsAcross) {
        this.securityAlertsAcross = securityAlertsAcross;
    }

    public GoCloudUser getGoCloudUser() {
        return goCloudUser;
    }

    public CloudGovernance goCloudUser(GoCloudUser goCloudUser) {
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
        if (!(o instanceof CloudGovernance)) {
            return false;
        }
        return id != null && id.equals(((CloudGovernance) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CloudGovernance{" +
            "id=" + getId() +
            ", governanceId='" + getGovernanceId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", activeApps=" + getActiveApps() +
            ", inActiveApps=" + getInActiveApps() +
            ", currentSpendAcross=" + getCurrentSpendAcross() +
            ", estimatedSpendAcross=" + getEstimatedSpendAcross() +
            ", securityAlertsAcross='" + getSecurityAlertsAcross() + "'" +
            "}";
    }
}
