//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Keine entsprechende Zielgruppe gefunden")
public class TargetGroupNotFoundException extends RuntimeException implements UIForwardable {

    public static final String REASON = "Keine entsprechende Zielgruppe gefunden";

    public TargetGroupNotFoundException() {
        super(REASON);
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
