//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Fehler im Aufbau der Idee")
public class IdeaMalformedException extends RuntimeException implements UIForwardable {

    public static final String REASON = "Fehler im Aufbau der Idee";

    public IdeaMalformedException() {
        super(REASON);
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
