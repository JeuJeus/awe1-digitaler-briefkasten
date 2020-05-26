//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Es ist ein interner Fehler aufgetreten")
public class InternalErrorException extends RuntimeException implements UIForwardable {

    public static final String REASON = "Es ist ein interner Fehler aufgetreten";

    public InternalErrorException() {
        super(REASON);
    }

    public InternalErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InternalErrorException(final String message) {
        super(message);
    }

    public InternalErrorException(final Throwable cause) {
        super(cause);
    }
}
