package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;

@Entity(name = "idea")
@JsonSerialize
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;

    //TODO not in inital project request -> ok?
    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String description;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User creator;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private java.sql.Date creationDate;

    //TODO ADD LOGIC FOR STORING IDEA / ACCEPTING / DECLINING
    @Column
    @Enumerated(EnumType.STRING)
    private Status status = Status.NOT_SUBMITTED;

    @Column
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String statusJustification;

    @PrePersist
    private void createdAt() {
        long millis = System.currentTimeMillis();
        this.creationDate = new java.sql.Date(millis);
    }

    public Idea() {
        super();
    }

    public Idea(String title, String description, User creator) {
        super();
        this.title = title;
        this.description = description;
        this.creator = creator;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getCreator() {
        return creator;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStatusJustification() {
        return statusJustification;
    }

    public void setStatusJustification(String statusJustification) {
        this.statusJustification = statusJustification;
    }
}
