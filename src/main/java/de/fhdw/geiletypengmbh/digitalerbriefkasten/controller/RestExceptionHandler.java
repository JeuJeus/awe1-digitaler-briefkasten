//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ContactMessage;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.DistributionChannel;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@EnableWebMvc
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleAllOthers(Exception ex, WebRequest request) throws Exception {
        if (!(ex instanceof InternalException)) {
            return handleExceptionInternal(ex, "Internal server error",
                    new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
        return handleException(ex, request);
    }

    @ExceptionHandler({IdeaNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "Idea not found",
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({IdeaIdMismatchException.class,
            ConstraintViolationException.class,
            DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleBadRequest(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getLocalizedMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


}