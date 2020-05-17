package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Keine entsprechende Zielgruppe gefunden")
public class TargetGroupNotFoundException extends RuntimeException {

    public TargetGroupNotFoundException() {
        super();
    }

    public TargetGroupNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TargetGroupNotFoundException(final String message) {
        super(message);
    }

    public TargetGroupNotFoundException(final Throwable cause) {
        super(cause);
    }
}
