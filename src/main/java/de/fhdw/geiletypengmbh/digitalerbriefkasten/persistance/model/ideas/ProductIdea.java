package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "productIdea")
public class ProductIdea extends Idea {
    private Long targetGroup;

    public Long getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(Long field) {
        this.targetGroup = field;
    }
}
