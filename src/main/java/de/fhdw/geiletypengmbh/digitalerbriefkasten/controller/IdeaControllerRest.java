//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ideas")
@PreAuthorize("hasRole('ROLE_API_USER')")
public class IdeaControllerRest {

    @Autowired
    private IdeaService ideaService;

    @GetMapping
    public Iterable<Idea> getSubmittedIdeas() {
        return ideaService.getSubmittedIdeas();
    }

    @GetMapping("/{id}")
    public Idea findOne(@PathVariable Long id) {
        return ideaService.findById(id);
    }
}
