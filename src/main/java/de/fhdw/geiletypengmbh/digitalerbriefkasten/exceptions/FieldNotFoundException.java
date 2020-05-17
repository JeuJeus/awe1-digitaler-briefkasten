package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Kein entsprechendes Handlungsfeld gefunden")
public class FieldNotFoundException extends RuntimeException {

    public FieldNotFoundException() {
        super();
    }

    public FieldNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FieldNotFoundException(final String message) {
        super(message);
    }

    public FieldNotFoundException(final Throwable cause) {
        super(cause);
    }
}
