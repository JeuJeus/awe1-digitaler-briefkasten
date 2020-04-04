package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //TODO not in inital project request -> ok?
    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private UUID creator;

    @Column(nullable = false)
    private java.sql.Date creationDate;

    public Idea() {
        super();
    }

    public Idea(String title, String description, UUID creator, java.sql.Date creationDate) {
        super();
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.creationDate = creationDate;
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

    public UUID getCreator() {
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

    public void setCreator(UUID creator) {
        this.creator = creator;
    }

    public void setCreationDate(java.sql.Date creationDate) {
        this.creationDate = creationDate;
    }
}
