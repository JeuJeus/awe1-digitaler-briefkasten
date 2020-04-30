package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas;

import javax.persistence.*;

@Entity
public class DistributionChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;

    @Column(nullable = false)
    private String title;

    public DistributionChannel() {
        super();
    }

    public DistributionChannel(String title) {
        super();
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String description) {
        this.title = description;
    }

}
