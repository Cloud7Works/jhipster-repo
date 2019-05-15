package com.cloud7works.gocloud.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CloudAssessment.
 */
@Entity
@Table(name = "cloud_assessment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cloudassessment")
public class CloudAssessment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "assessment_id")
    private String assessmentId;

    @Column(name = "question_id")
    private String questionId;

    @Column(name = "question")
    private String question;

    @Column(name = "choice")
    private String choice;

    @Column(name = "question_objective")
    private String questionObjective;

    @Column(name = "user_selection")
    private String userSelection;

    @Column(name = "user_id")
    private String userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public CloudAssessment assessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
        return this;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public CloudAssessment questionId(String questionId) {
        this.questionId = questionId;
        return this;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public CloudAssessment question(String question) {
        this.question = question;
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoice() {
        return choice;
    }

    public CloudAssessment choice(String choice) {
        this.choice = choice;
        return this;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getQuestionObjective() {
        return questionObjective;
    }

    public CloudAssessment questionObjective(String questionObjective) {
        this.questionObjective = questionObjective;
        return this;
    }

    public void setQuestionObjective(String questionObjective) {
        this.questionObjective = questionObjective;
    }

    public String getUserSelection() {
        return userSelection;
    }

    public CloudAssessment userSelection(String userSelection) {
        this.userSelection = userSelection;
        return this;
    }

    public void setUserSelection(String userSelection) {
        this.userSelection = userSelection;
    }

    public String getUserId() {
        return userId;
    }

    public CloudAssessment userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CloudAssessment)) {
            return false;
        }
        return id != null && id.equals(((CloudAssessment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CloudAssessment{" +
            "id=" + getId() +
            ", assessmentId='" + getAssessmentId() + "'" +
            ", questionId='" + getQuestionId() + "'" +
            ", question='" + getQuestion() + "'" +
            ", choice='" + getChoice() + "'" +
            ", questionObjective='" + getQuestionObjective() + "'" +
            ", userSelection='" + getUserSelection() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
