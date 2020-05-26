//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.InternalErrorException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.UIForwardable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({Exception.class})
    protected ModelAndView handleException(Exception ex, HttpServletResponse response) {

        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        response.setStatus(responseStatus.code().value());
        ModelAndView mav = new ModelAndView("error");
        if (ex instanceof UIForwardable) {
            mav.addObject("exception", ex);
        } else {
            mav.addObject("exception", new InternalErrorException());
        }
        return mav;
    }
}