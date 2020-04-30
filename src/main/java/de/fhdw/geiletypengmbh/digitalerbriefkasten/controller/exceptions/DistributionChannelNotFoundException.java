package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No Matching Distribution-Channel Found")
public class DistributionChannelNotFoundException extends RuntimeException {

    public DistributionChannelNotFoundException() {
        super();
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
