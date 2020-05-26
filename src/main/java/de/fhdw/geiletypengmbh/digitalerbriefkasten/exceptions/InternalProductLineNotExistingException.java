//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Es wurde noch kein Vertriebskanal für interne Ideen angelegt")
public class InternalProductLineNotExistingException extends Exception implements UIForwardable {

    public static final String REASON = "Es wurde noch kein Vertriebskanal für interne Ideen angelegt";

    public InternalProductLineNotExistingException() {
        super(REASON);
    }

    public InternalProductLineNotExistingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InternalProductLineNotExistingException(final String message) {
        super(message);
    }

    public InternalProductLineNotExistingException(final Throwable cause) {
        super(cause);
    }
}
