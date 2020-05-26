//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.InternalErrorException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.UIForwardable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({Exception.class})
    protected ModelAndView handleException(Exception ex) {
        ModelAndView mav = new ModelAndView("error");
        if (ex instanceof UIForwardable) {
            mav.addObject("exception", ex);
        } else {
            mav.addObject("exception", new InternalErrorException());
        }
        return mav;

    }

}