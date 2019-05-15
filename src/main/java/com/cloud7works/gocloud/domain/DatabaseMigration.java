package com.cloud7works.gocloud.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DatabaseMigration.
 */
@Entity
@Table(name = "database_migration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "databasemigration")
public class DatabaseMigration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "db_name")
    private String dbName;

    @Column(name = "db_user_name")
    private String dbUserName;

    @Column(name = "db_pass_word")
    private String dbPassWord;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDbName() {
        return dbName;
    }

    public DatabaseMigration dbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public DatabaseMigration dbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
        return this;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassWord() {
        return dbPassWord;
    }

    public DatabaseMigration dbPassWord(String dbPassWord) {
        this.dbPassWord = dbPassWord;
        return this;
    }

    public void setDbPassWord(String dbPassWord) {
        this.dbPassWord = dbPassWord;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DatabaseMigration)) {
            return false;
        }
        return id != null && id.equals(((DatabaseMigration) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DatabaseMigration{" +
            "id=" + getId() +
            ", dbName='" + getDbName() + "'" +
            ", dbUserName='" + getDbUserName() + "'" +
            ", dbPassWord='" + getDbPassWord() + "'" +
            "}";
    }
}
