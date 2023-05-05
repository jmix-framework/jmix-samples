package com.company.library.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@JmixEntity
@Table(name = "JMXRPR_INIT_FLAGS")
@Entity(name = "jmxrpr_InitFlags")
public class InitFlags {
    @Column(name = "ID", nullable = false)
    @Id
    private Integer id;

    @Column(name = "REPORTS_INITIALIZED")
    private Boolean reportsInitialized;

    public Boolean getReportsInitialized() {
        return reportsInitialized;
    }

    public void setReportsInitialized(Boolean reportsInitialized) {
        this.reportsInitialized = reportsInitialized;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}