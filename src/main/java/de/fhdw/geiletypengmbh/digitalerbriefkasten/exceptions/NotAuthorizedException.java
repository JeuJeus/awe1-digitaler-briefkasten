//Autor: JB
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Nicht berechtigt")
public class NotAuthorizedException extends RuntimeException implements UIForwardable {

    public static final String REASON = "Nicht berechtigt";

    public NotAuthorizedException() {
        super(REASON);
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
