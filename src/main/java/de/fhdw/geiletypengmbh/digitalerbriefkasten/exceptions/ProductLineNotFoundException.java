//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Keine entsprechende Produktsparte gefunden")
public class ProductLineNotFoundException extends RuntimeException implements UIForwardable {

    public static final String reason = "Keine entsprechende Produktsparte gefunden";

    public ProductLineNotFoundException() {
        super(reason);
    }

    public ProductLineNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ProductLineNotFoundException(final String message) {
        super(message);
    }

    public ProductLineNotFoundException(final Throwable cause) {
        super(cause);
    }
}
