package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.InternalProductLineNotExistingException;
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
        return ideaService.createByForm(idea);
    }

    @PostMapping("/product")
    public Idea createProductIdea(@ModelAttribute ProductIdea idea) {
        try {
            return ideaService.createByForm(idea);
        } catch (InternalProductLineNotExistingException ignored) {
            //ignore InternalProductLineNotExistingException because backend method throws it
            // (wont throw in this case anyways)
        }
        return new Idea();
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @PostMapping("/decideIdea/{id}")
    public Idea decideIdea(@ModelAttribute Idea emptyIdeaWithDecision, @PathVariable Long id) {
        return ideaService.saveDecision(id, emptyIdeaWithDecision);
    }

    @PostMapping("/update/internal/{id}")
    public Idea updateInternalIdeaByForm(@ModelAttribute InternalIdea idea, @PathVariable Long id) {
        return ideaService.saveUpdateIdea(id, idea);
    }

    @PostMapping("/update/product/{id}")
    public Idea updateProductIdeaByForm(@ModelAttribute ProductIdea idea, @PathVariable Long id) {
        return ideaService.saveUpdateIdea(id, idea);
    }

}


