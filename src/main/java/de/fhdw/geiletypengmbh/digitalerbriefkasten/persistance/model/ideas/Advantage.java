package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas;

import javax.persistence.*;

@Entity
public class Advantage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;

    @Column(nullable = false)
    private String description;

    public Advantage() {
        super();
    }

    public Advantage(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
