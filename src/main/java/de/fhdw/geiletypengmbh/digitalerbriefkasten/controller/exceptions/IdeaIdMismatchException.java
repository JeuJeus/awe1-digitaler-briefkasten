package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions;

public class IdeaIdMismatchException extends RuntimeException {

    public IdeaIdMismatchException() {
        super();
    }

    public IdeaIdMismatchException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IdeaIdMismatchException(final String message) {
        super(message);
    }

    public IdeaIdMismatchException(final Throwable cause) {
        super(cause);
    }
}
