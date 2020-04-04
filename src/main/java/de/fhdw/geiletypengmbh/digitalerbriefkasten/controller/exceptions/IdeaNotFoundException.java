package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No Matching Idea Found")
public class IdeaNotFoundException extends RuntimeException {

    public IdeaNotFoundException() {
        super();
    }

    public IdeaNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IdeaNotFoundException(final String message) {
        super(message);
    }

    public IdeaNotFoundException(final Throwable cause) {
        super(cause);
    }
}
