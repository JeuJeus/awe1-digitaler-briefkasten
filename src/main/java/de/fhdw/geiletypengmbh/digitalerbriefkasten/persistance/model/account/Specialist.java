//Autor: JB
package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@JsonSerialize
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Specialist extends User {
    @JsonIgnoreProperties("specialists")
    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "specialist_productLines",
            joinColumns = @JoinColumn(name = "specialist_id"),
            inverseJoinColumns = @JoinColumn(name = "productLine_id"))
    private List<ProductLine> productLines;

    public Specialist() {
        super();
    }

    public Specialist(String username, String password, String passwordConfirmation) {
        super(username, password, passwordConfirmation);
    }

    public Specialist(String username, String password, String passwordConfirmation, String firstName, String lastName) {
        super(username, password, passwordConfirmation, firstName, lastName);
    }

    public List<ProductLine> getProductLines() {
        return productLines;
    }

    public void setProductLines(List<ProductLine> productLines) {
        this.productLines = productLines;
    }
}
