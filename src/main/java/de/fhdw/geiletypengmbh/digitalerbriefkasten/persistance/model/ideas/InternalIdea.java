package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;

//TODO CHANGE TABLES/INITIALIZATION OF IDEA OBJECT
@JsonSerialize
public class InternalIdea extends Idea {

    //TODO REFACTOR ME TO BE OWN OBJECT
    @Column
    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
