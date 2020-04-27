package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "internalIdea")
public class InternalIdea extends Idea {
    private Long field;

    public Long getField() {
        return field;
    }

    public void setField(Long field) {
        this.field = field;
    }
}
