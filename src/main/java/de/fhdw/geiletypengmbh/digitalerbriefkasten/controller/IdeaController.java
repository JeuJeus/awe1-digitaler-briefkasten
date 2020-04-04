package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaIdMismatchException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaMalformedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ideas")
public class IdeaController {

    @Autowired
    private IdeaRepository ideaRepository;

    @GetMapping
    public Iterable<Idea> findAll() {
        return ideaRepository.findAll();
    }

    @GetMapping("/title/{ideaTitle}")
    public List<Idea> findByTitle(@PathVariable String ideaTitle) {
        return ideaRepository.findByTitle(ideaTitle);
    }

    @GetMapping("/{id}")
    public Idea findOne(@PathVariable Long id) {
        return ideaRepository.findById(id)
                .orElseThrow(IdeaNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Idea create(@RequestBody Idea idea) {
        try {
            return ideaRepository.save(idea);
        } catch (Exception e) {
            //TODO refactor thrown Exception not to be as generous
            throw new IdeaMalformedException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ideaRepository.findById(id)
                .orElseThrow(IdeaNotFoundException::new);
        ideaRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Idea updateIdea(@RequestBody Idea idea, @PathVariable Long id) {
        if (idea.getId() != id) {
            throw new IdeaIdMismatchException();
        }
        ideaRepository.findById(id)
                .orElseThrow(IdeaNotFoundException::new);
        return ideaRepository.save(idea);
    }

}
