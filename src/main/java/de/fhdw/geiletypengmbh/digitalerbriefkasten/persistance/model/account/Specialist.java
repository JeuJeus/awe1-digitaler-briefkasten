package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;

import javax.persistence.*;
import java.util.Set;

@JsonSerialize
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Specialist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private long id;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<ProductLine> productLines;
}
