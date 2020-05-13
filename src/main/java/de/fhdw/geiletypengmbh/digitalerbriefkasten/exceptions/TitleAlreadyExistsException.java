package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Title already exists")
public class TitleAlreadyExistsException extends RuntimeException {

    public TitleAlreadyExistsException() {
        super();
    }

    public TitleAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TitleAlreadyExistsException(final String message) {
        super(message);
    }

    public TitleAlreadyExistsException(final Throwable cause) {
        super(cause);
    }
}
