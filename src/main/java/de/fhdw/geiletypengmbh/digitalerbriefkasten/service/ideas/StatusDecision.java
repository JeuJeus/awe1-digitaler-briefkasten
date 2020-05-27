package de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Status;

public class StatusDecision {

    private Status status = Status.NOT_SUBMITTED;

    private String statusJustification;

    public StatusDecision() {

    }

    public StatusDecision(Status status, String statusJustification) {
        super();
        this.status = status;
        this.statusJustification = statusJustification;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStatusJustification() {
        return statusJustification;
    }

    public void setStatusJustification(String statusJustification) {
        this.statusJustification = statusJustification;
    }
}
