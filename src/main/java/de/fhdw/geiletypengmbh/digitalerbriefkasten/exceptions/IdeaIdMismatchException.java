package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "ID der eingegebenen Idee ist nicht korrekt")
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
