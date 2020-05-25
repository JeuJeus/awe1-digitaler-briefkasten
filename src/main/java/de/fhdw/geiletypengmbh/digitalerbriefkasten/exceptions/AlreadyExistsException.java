//Autor: JB
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Titel existiert bereits")
public class AlreadyExistsException extends RuntimeException implements InternalException {

    public AlreadyExistsException() {
        super();
    }

    public AlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistsException(final String message) {
        super(message);
    }

    public AlreadyExistsException(final Throwable cause) {
        super(cause);
    }
}
