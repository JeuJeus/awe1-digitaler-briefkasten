package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Malformed Idea")
public class IdeaMalformedException extends RuntimeException {

    public IdeaMalformedException() {
        super();
    }

    public IdeaMalformedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IdeaMalformedException(final String message) {
        super(message);
    }

    public IdeaMalformedException(final Throwable cause) {
        super(cause);
    }
}
