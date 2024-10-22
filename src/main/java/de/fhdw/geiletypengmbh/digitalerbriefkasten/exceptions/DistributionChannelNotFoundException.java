//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Kein entsprechender Vertriebskanal gefunden")
public class DistributionChannelNotFoundException extends RuntimeException implements UIForwardable {

    public static final String REASON = "Kein entsprechender Vertriebskanal gefunden";

    public DistributionChannelNotFoundException() {
        super(REASON);
    }

    public DistributionChannelNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DistributionChannelNotFoundException(final String message) {
        super(message);
    }

    public DistributionChannelNotFoundException(final Throwable cause) {
        super(cause);
    }
}

