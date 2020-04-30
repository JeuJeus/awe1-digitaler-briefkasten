package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;

import javax.persistence.*;
import java.util.Set;

@Entity
public class ProductLine {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @JsonIgnore
    @ManyToMany(mappedBy = "productLines", fetch = FetchType.EAGER)
    private Set<Idea> ideas;

    @JsonIgnore
    @ManyToMany(mappedBy = "productLines", fetch = FetchType.EAGER)
    private Set<Specialist> specialists;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProductLine(String title) {
        this.title = title;
    }

    public ProductLine() {
    }

    ;
}

