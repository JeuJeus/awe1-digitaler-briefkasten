package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@JsonSerialize
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class InternalIdea extends Idea {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "field_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Field field;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getType(){
        return  this.getClass().getSimpleName();
    }
}
