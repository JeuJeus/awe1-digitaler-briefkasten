package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProductLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String title;

    @JsonIgnoreProperties("productLines")
    @ManyToMany(mappedBy = "productLines", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Specialist> specialists;

    public ProductLine(String title) {
        this.title = title;
    }

    public ProductLine(String title, List<Specialist> specialists) {
        this.title = title;
        this.specialists = specialists;
    }

    public ProductLine() {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Specialist> getSpecialists() {
        return specialists;
    }

    public void setSpecialists(List<Specialist> specialists) {
        this.specialists = specialists;
    }

    ;
}

