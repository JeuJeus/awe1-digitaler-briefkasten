package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.IdeaService;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        //TODO WHEN IMPLEMENTING SECURITY NOT ANYBODY SHOULD BE ALLOWED TO CREATE
        return ideaService.save(idea);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        //TODO WHEN IMPLEMENTING SECURITY NOT ANYBODY SHOULD BE ALLOWED TO DELETE
        ideaService.delete(id);
    }

    @PutMapping("/{id}")
    public Idea updateIdea(@RequestBody Idea idea, @PathVariable Long id) {
        //TODO NOT ANY VALUE SHOULD BE POSSIBLE TO BE UPDATED
        return ideaService.updateIdea(idea, id);
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public Idea createByForm(@ModelAttribute Idea idea) {
        //TODO DEPECREATED -> MAKE ADDIDEA USE JSON
        return ideaService.createByForm(idea);
    }
}
