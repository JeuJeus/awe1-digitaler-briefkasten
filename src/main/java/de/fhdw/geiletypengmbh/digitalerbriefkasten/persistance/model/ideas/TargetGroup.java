package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Entity
public class TargetGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;

    @Column(nullable = false)
    private String title;

    @JsonIgnoreProperties("productIdeas")
    @ManyToMany(mappedBy = "targetGroups", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<ProductIdea> productIdeas;

    public TargetGroup() {
        super();
    }

    public TargetGroup(String title) {
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

    public Set<ProductIdea> getProductIdeas() {
        return productIdeas;
    }

    public void setProductIdeas(Set<ProductIdea> productIdeas) {
        this.productIdeas = productIdeas;
    }
}
