//Autor: JB
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Keine entsprechende Idee gefunden")
public class IdeaNotFoundException extends RuntimeException implements UIForwardable {

    public static final String REASON = "Keine entsprechende Idee gefunden";

    public IdeaNotFoundException() {
        super(REASON);
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
