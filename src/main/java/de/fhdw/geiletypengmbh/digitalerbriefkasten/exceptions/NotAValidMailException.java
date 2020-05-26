//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Eingegebene Mailadresse ist fehlerhaft")
public class NotAValidMailException extends RuntimeException implements UIForwardable {

    public static final String reason = "Eingegebene Mailadresse ist fehlerhaft";

    public NotAValidMailException() {
        super(reason);
    }

    public NotAValidMailException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotAValidMailException(final String message) {
        super(message);
    }

    public NotAValidMailException(final Throwable cause) {
        super(cause);
    }
}
