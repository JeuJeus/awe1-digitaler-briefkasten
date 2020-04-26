package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaIdMismatchException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaMalformedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.NotAuthorizedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Status;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class IdeaService {

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private UserService userService;

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
        //Only logged in Users are able to create Ideas
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            try {
                return ideaRepository.saveAndFlush(idea);
            } catch (Exception e) {
                throw new IdeaMalformedException(e);
            }
        } else {
            throw new NotAuthorizedException();
        }
    }

    public void delete(Long id, HttpServletRequest request) {
        Idea toDelete = ideaRepository.findById(id)
                .orElseThrow(IdeaNotFoundException::new);
        User currentUser = getCurrentUser();

        /* There are 2 legitimate states to delete a Idea
            -> either as an ADMIN "with Godpower"
            -> or as THE CREATOR and if the idea was NOT_SUBMITTED (= still in draftmode)
        */
        if (request.isUserInRole("ADMIN")) {
            ideaRepository.deleteById(id);
        } else if (toDelete.getCreator().equals(currentUser)
                && toDelete.getStatus().equals(Status.NOT_SUBMITTED)) {
            ideaRepository.deleteById(id);
        } else {
            throw new NotAuthorizedException();
        }
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

        Predicate<Idea> ideaBelongsToCurUser = idea -> idea.getCreator().getId() == getCurrentUser().getId();
        Predicate<Idea> ideaIsNotSubmitted = idea -> idea.getStatus().equals(Status.NOT_SUBMITTED);

        return allIdeas.stream().
                filter(ideaBelongsToCurUser.and(ideaIsNotSubmitted))
                .collect(Collectors.toList());
    }
}
