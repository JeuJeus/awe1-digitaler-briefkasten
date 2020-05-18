//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas;

public enum Status {
    NOT_SUBMITTED("Nicht eingereicht"),
    PENDING("auf Bewertung wartend"),
    ACCEPTED("Angenommen"),
    DECLINED("Abgelehnt"),
    IDEA_STORAGE("Im Ideenspeicher");

    public final String displayValue;

    Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
