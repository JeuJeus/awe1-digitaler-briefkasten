package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.InternalProductLineNotExistingException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Role;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.IdeaService;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/ideas")
public class IdeaController {

    @Autowired
    private IdeaService ideaService;

    @GetMapping
    public Iterable<Idea> findAll() {
        return ideaService.findAll();
    }

    @GetMapping("/title/{ideaTitle}")
    public List<Idea> findByTitle(@PathVariable String ideaTitle) {
        return ideaService.findByTitle(ideaTitle);
    }

    @GetMapping("/{id}")
    public Idea findOne(@PathVariable Long id) {
        return ideaService.findById(id);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Idea create(@RequestBody Idea idea) {
        return ideaService.save(idea);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, HttpServletRequest request) {
        ideaService.delete(id, request);
    }

    @PutMapping("/{id}")
    public Idea updateIdea(@RequestBody Idea idea, @PathVariable Long id) {
        //TODO NOT ANY VALUE SHOULD BE POSSIBLE TO BE UPDATED
        return ideaService.updateIdea(idea, id);
    }

    @PostMapping("/internal")
    public Idea createInternalIdea(@ModelAttribute InternalIdea idea) throws InternalProductLineNotExistingException {
        //TODO DEPECREATED -> MAKE ADDIDEA USE JSON
        return ideaService.createByForm(idea);
    }

    @PostMapping("/product")
    public Idea createProductIdea(@ModelAttribute ProductIdea idea) {
        //TODO DEPECREATED -> MAKE ADDIDEA USE JSON
        try {
            return ideaService.createByForm(idea);
        } catch (InternalProductLineNotExistingException ignored) {
        }
        return new Idea();
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @PostMapping("/decideIdea")
    public void decideIdea(@ModelAttribute InternalIdea idea){
        //TODO DEPECREATED -> MAKE DECIDEIDEA USE JSON
        ideaService.saveDecision(idea);
    }

}


