//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.IdeaNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.InternalErrorException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.NotAuthorizedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.UIForwardable;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/ideas")
@PreAuthorize("hasRole('ROLE_API_USER')")
public class IdeaControllerRest {

    @Autowired
    private IdeaService ideaService;

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public Iterable<Idea> findAll() {
        return ideaService.findAll();
    }

    @GetMapping("/title/{ideaTitle}")
    public List<Idea> findByTitle(@PathVariable String ideaTitle) {
        return ideaService.findByTitle(ideaTitle);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Idea create(@RequestBody Idea idea) {
        return ideaService.clearIdAndSave(idea);
    }

    @GetMapping("/submitted")
    public Iterable<Idea> getSubmittedIdeas() {
        return ideaService.getSubmittedIdeas();
    }

    @GetMapping("/{id}")
    public Idea findOne(@PathVariable Long id) {
        return ideaService.findById(id);
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, HttpServletRequest request) {
        ideaService.delete(id, request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Idea updateIdea(@RequestBody Idea idea, @PathVariable Long id) {
        return ideaService.updateIdea(idea, id);
    }


    // Autor: PR
    // Overwrites global Exceptionhandler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e, HttpServletRequest request) throws Exception {
        ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
        if (e instanceof UIForwardable) {
            return error(e, request);
        } else {
            return error(new InternalErrorException(), request);
        }
    }

    // Autor: PR
    private ResponseEntity<String> error(Exception e, HttpServletRequest request) {
        ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode obj = mapper.createObjectNode();
        obj.put("timestamp", String.valueOf(new Timestamp(System.currentTimeMillis())));
        obj.put("status", responseStatus.code().value());
        obj.put("error", responseStatus.code().getReasonPhrase());
        obj.put("message", responseStatus.reason());
        obj.put("path", request.getRequestURI());
        return new ResponseEntity<String>(obj.toString(), responseStatus.code());
    }
}
