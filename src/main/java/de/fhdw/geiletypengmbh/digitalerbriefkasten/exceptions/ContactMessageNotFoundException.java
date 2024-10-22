//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Keine entsprechende Kontakt-Nachricht gefunden")
public class ContactMessageNotFoundException extends RuntimeException implements UIForwardable {

    public static final String REASON = "Keine entsprechende Kontakt-Nachricht gefunden";

    public ContactMessageNotFoundException() {
        super(REASON);
    }

    public ContactMessageNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ContactMessageNotFoundException(final String message) {
        super(message);
    }

    public ContactMessageNotFoundException(final Throwable cause) {
        super(cause);
    }
}
