package com.company.library.entity;

import io.jmix.core.annotation.DeletedBy;
import io.jmix.core.annotation.DeletedDate;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "JMXRPR_BOOK", indexes = {
        @Index(name = "IDX_JMXRPRBOOK_LITERATURETYPE", columnList = "LITERATURE_TYPE_ID")
})
@Entity(name = "jmxrpr_Book")
public class Book {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @JoinTable(name = "JMXRPR_BOOK_AUTHOR_LINK",
            joinColumns = @JoinColumn(name = "BOOK_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<Author> authors;

    @JoinColumn(name = "LITERATURE_TYPE_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private LiteratureType literatureType;

    @InstanceName
    @Column(name = "NAME", nullable = false, length = 50)
    @NotNull
    private String name;

    @Column(name = "SUMMARY")
    @Lob
    private String summary;

    @DeletedBy
    @Column(name = "DELETED_BY")
    private String deletedBy;

    @DeletedDate
    @Column(name = "DELETED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LiteratureType getLiteratureType() {
        return literatureType;
    }

    public void setLiteratureType(LiteratureType literatureType) {
        this.literatureType = literatureType;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}