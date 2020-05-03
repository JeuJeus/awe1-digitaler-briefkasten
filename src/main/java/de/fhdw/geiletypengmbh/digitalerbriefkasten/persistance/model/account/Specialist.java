package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;

import javax.persistence.*;
import java.util.Set;

@JsonSerialize
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Specialist extends User {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "specialist_productLines",
            joinColumns = @JoinColumn(name = "specialist_id"),
            inverseJoinColumns = @JoinColumn(name = "productLine_id"))
    private Set<ProductLine> productLines;

    public Specialist() {
        super();
    }

    public Specialist(String username, String password, String passwordConfirmation) {
        super(username, password, passwordConfirmation);
    }

    public Set<ProductLine> getProductLines() {
        return productLines;
    }

    public void setProductLines(Set<ProductLine> productLines) {
        this.productLines = productLines;
    }
}
