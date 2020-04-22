package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaIdMismatchException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaMalformedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdeaService {

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private UserService userService;


    public Iterable<Idea> findAll() {
        return ideaRepository.findAll();
    }

    public List<Idea> findByTitle(String ideaTitle) {
        return ideaRepository.findByTitle(ideaTitle);
    }

    public Idea findById(Long id) {
        return ideaRepository.findById(id)
                .orElseThrow(IdeaNotFoundException::new);
    }

    public Idea save(Idea idea) {
        try {
            return ideaRepository.saveAndFlush(idea);
        } catch (Exception e) {
            //TODO refactor thrown Exception not to be as generous
            throw new IdeaMalformedException(e);
        }
    }

    public void delete(Long id) {
        //TODO when implementing security not anybody should be allowed to delete
        ideaRepository.findById(id)
                .orElseThrow(IdeaNotFoundException::new);
        ideaRepository.deleteById(id);
    }

    public Idea updateIdea(Idea idea, Long id) {
        if (idea.getId() != id) {
            throw new IdeaIdMismatchException();
        }
        ideaRepository.findById(id)
                .orElseThrow(IdeaNotFoundException::new);
        return ideaRepository.saveAndFlush(idea);
    }

    public Idea createByForm(Idea idea) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User creator = userService.findByUsername(username);
        idea.setCreator(creator);

        return ideaRepository.saveAndFlush(idea);
    }
}
