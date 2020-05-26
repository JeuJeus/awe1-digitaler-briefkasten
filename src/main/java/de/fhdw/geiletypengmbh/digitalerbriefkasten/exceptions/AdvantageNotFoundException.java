//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Kein entsprechender Vorteil gefunden")
public class AdvantageNotFoundException extends RuntimeException implements UIForwardable {

    public static final String REASON = "Kein entsprechender Vorteil gefunden";

    public AdvantageNotFoundException() {
        super(REASON);
    }

    public AdvantageNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AdvantageNotFoundException(final String message) {
        super(message);
    }

    public AdvantageNotFoundException(final Throwable cause) {
        super(cause);
    }
}
