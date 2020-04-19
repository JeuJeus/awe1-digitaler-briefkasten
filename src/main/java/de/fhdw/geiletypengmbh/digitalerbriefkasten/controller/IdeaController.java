package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaIdMismatchException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaMalformedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import static org.aspectj.bridge.Version.getTime;

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
        //TODO when implementing security not anybody should be allowed to create
        try {
            return ideaRepository.save(idea);
        } catch (Exception e) {
            //TODO refactor thrown Exception not to be as generous
            throw new IdeaMalformedException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        //TODO when implementing security not anybody should be allowed to delete
        ideaRepository.findById(id)
                .orElseThrow(IdeaNotFoundException::new);
        ideaRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Idea updateIdea(@RequestBody Idea idea, @PathVariable Long id) {
        //TODO not any value should be possible to be updated
        if (idea.getId() != id) {
            throw new IdeaIdMismatchException();
        }
        ideaRepository.findById(id)
                .orElseThrow(IdeaNotFoundException::new);
        return ideaRepository.save(idea);
    }

    @PostMapping("/addIdea")
    public String addIdea(@ModelAttribute Idea idea) {
        java.sql.Date sqlDate = new java.sql.Date(getTime());//TODO time fixen
        idea.setCreationDate(sqlDate);
        idea.setCreator(UUID.randomUUID()); //TODO user anlegen fixen
        ideaRepository.save(idea);
        return idea.getTitle() + " und ID ist " + idea.getId();
    }

}
