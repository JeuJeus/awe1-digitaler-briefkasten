package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaIdMismatchException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaMalformedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Status;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class IdeaService {

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private UserServiceImpl userService;

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return userService.findByUsername(username);
    }

    public List<Idea> findAll() {
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
        //TODO WHEN IMPLEMENTING SECURITY NOT ANYBODY SHOULD BE ALLOWED TO DELETE
        ideaRepository.findById(id)
                .orElseThrow(IdeaNotFoundException::new);
        /*Idea toDelete = ideaRepository.findById(id)
                .orElseThrow(IdeaNotFoundException::new);
        User currentUser = getCurrentUser();
        if(toDelete.getCreator().equals(currentUser) || currentUser.getRoles().equals(ADMINROLE/SPECIALISTROLE)){
            THEN DELETE
        }*/
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
        idea.setCreator(getCurrentUser());
        return save(idea);
    }

    public List<Idea> getSubmittedIdeas() {
        List<Idea> allIdeas = findAll();
        Predicate<Idea> ideaIsNotSubmitted = idea -> idea.getStatus().equals(Status.NOT_SUBMITTED);

        return allIdeas.stream().
                filter(Predicate.not(ideaIsNotSubmitted))
                .collect(Collectors.toList());
    }

    public List<Idea> GetOwnNotSubmittedIdeas() {
        List<Idea> allIdeas = findAll();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String securityUsername = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(securityUsername);

        Predicate<Idea> ideaBelongsToCurUser = idea -> idea.getCreator().getId() == user.getId();
        Predicate<Idea> ideaIsNotSubmitted = idea -> idea.getStatus().equals(Status.NOT_SUBMITTED);

        return allIdeas.stream().
                filter(ideaBelongsToCurUser.and(ideaIsNotSubmitted))
                .collect(Collectors.toList());
    }
}
