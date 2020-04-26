package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Not authorized to delete!")
public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException() {
        super();
    }

    public NotAuthorizedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotAuthorizedException(final String message) {
        super(message);
    }

    public NotAuthorizedException(final Throwable cause) {
        super(cause);
    }
}
