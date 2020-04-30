package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No Matching Advantage Found")
public class AdvantageNotFoundException extends RuntimeException {

    public AdvantageNotFoundException() {
        super();
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
