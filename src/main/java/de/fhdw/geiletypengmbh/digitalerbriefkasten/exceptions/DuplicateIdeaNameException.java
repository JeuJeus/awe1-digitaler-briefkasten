//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Es existiert bereits eine Idee mit diesem Namen")
public class DuplicateIdeaNameException extends RuntimeException implements UIForwardable {

    public static final String REASON = "Es existiert bereits eine Idee mit diesem Namen";

    public DuplicateIdeaNameException() {
        super(REASON);
    }

    public DuplicateIdeaNameException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DuplicateIdeaNameException(final String message) {
        super(message);
    }

    public DuplicateIdeaNameException(final Throwable cause) {
        super(cause);
    }
}
