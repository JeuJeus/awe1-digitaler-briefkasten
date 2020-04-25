package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Not authorized to delete!")
public class NotAuthorizedToDeleteException extends RuntimeException {

    public NotAuthorizedToDeleteException() {
        super();
    }

    public NotAuthorizedToDeleteException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotAuthorizedToDeleteException(final String message) {
        super(message);
    }

    public NotAuthorizedToDeleteException(final Throwable cause) {
        super(cause);
    }
}
