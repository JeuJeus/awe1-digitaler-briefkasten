package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.IdeaService;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.UserService;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        //TODO when implementing security not anybody should be allowed to create
        return ideaService.save(idea);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        //TODO when implementing security not anybody should be allowed to delete
        ideaService.delete(id);
    }

    @PutMapping("/{id}")
    public Idea updateIdea(@RequestBody Idea idea, @PathVariable Long id) {
        //TODO not any value should be possible to be updated
        return ideaService.updateIdea(idea, id);
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public Idea createByForm(@ModelAttribute Idea idea) {
        //TODO DEPECREATED -> MAKE ADDIDEA USE JSON
        return ideaService.createByForm(idea);
    }
}
